package dev.tantto.maistempo.Google

import android.net.Uri
import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dev.tantto.maistempo.Chaves.Chaves
import dev.tantto.maistempo.Modelos.Lojas
import dev.tantto.maistempo.Modelos.Perfil
import java.math.BigDecimal
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.functions.FirebaseFunctions
import java.math.BigInteger

enum class TipoPontos(val valor:String){
    PONTOS_FILA("pontosFila"),
    PONTOS_AVALIACAO("pontosLocais"),
    PONTOS_TOTAIS("pontosTotais")
}

enum class Respostas(val valor: String){
    SUCESSO("sucesso"),
    ERRO("erro")
}

class DatabaseFirebaseSalvar {

    val BancoFirestore = FirebaseFirestore.getInstance()

    companion object {

        fun salvarDados(Dados:Perfil){
            FirebaseFirestore.getInstance().collection(Chaves.CHAVE_USUARIO.valor).document(Dados.email).set(Dados)
        }

        fun adicionarPontos(Email: String, Pontos:Int, Tipo:TipoPontos){
            val Documento = FirebaseFirestore.getInstance().collection(Chaves.CHAVE_USUARIO.valor).document(Email).get()
            Documento.addOnSuccessListener {
                val valorRecuperado = it.get(Tipo.valor).toString().toLong()
                val fila = it.get(TipoPontos.PONTOS_FILA.valor).toString().toLong()
                val avaliacao = it.get(TipoPontos.PONTOS_AVALIACAO.valor).toString().toLong()
                FirebaseFirestore.getInstance().collection(Chaves.CHAVE_USUARIO.valor).document(Email).update(Tipo.valor, Pontos + valorRecuperado)
                FirebaseFirestore.getInstance().collection(Chaves.CHAVE_USUARIO.valor).document(Email).update(TipoPontos.PONTOS_TOTAIS.valor, fila + avaliacao + 1)
            }

        }

        fun adicionarNotaFila(Nome:String, Valor:Int){
            val Documento = FirebaseFirestore.getInstance().collection(Chaves.CHAVE_LOJA.valor).document(Nome).get()
            Documento.addOnSuccessListener {

            }
        }

        fun mudarRaio(Email: String, Valor: Int){
            FirebaseFirestore.getInstance().collection(Chaves.CHAVE_USUARIO.valor).document(Email).update(Chaves.CHAVE_RAIO.valor, Valor)
        }

        fun mudarNomeComImagem(Email: String, Nome: String = "", Caminho:Uri = Uri.EMPTY, Resposta: DatabaseMudanca){
            if(Nome.isNotEmpty() && Caminho == Uri.EMPTY){
                FirebaseFirestore.getInstance().collection(Chaves.CHAVE_USUARIO.valor).document(Email).update(Chaves.CHAVE_TITULO.valor, Nome).addOnCompleteListener {
                    if(it.isSuccessful){
                        Resposta.Resposta(Respostas.SUCESSO)
                    } else {
                        Resposta.Resposta(Respostas.ERRO)
                    }
                }
            } else if(Nome.isEmpty() && Caminho != Uri.EMPTY){
                CloudStorageFirebase.mudarImagem(Caminho, Email).addOnCompleteListener {
                    if(it.isSuccessful){
                        Resposta.Resposta(Respostas.SUCESSO)
                    } else {
                        Resposta.Resposta(Respostas.ERRO)
                    }
                }
            } else {
                FirebaseFirestore.getInstance().collection(Chaves.CHAVE_USUARIO.valor).document(Email).update(Chaves.CHAVE_TITULO.valor, Nome).addOnCompleteListener {
                    if(it.isSuccessful){
                        Resposta.Resposta(Respostas.SUCESSO)
                        CloudStorageFirebase.mudarImagem(Caminho, Email).addOnCompleteListener {
                            if(it.isSuccessful){
                                Resposta.Resposta(Respostas.SUCESSO)
                            } else {
                                Resposta.Resposta(Respostas.ERRO)
                            }
                        }
                    } else {
                        Resposta.Resposta(Respostas.ERRO)
                    }
                }
            }
        }

        fun deletarConta(Email: String){
            FirebaseFirestore.getInstance().collection(Chaves.CHAVE_USUARIO.valor).document(Email).delete()
            FirebaseDatabase.getInstance().reference.child(Email).removeValue()
        }
    }

}

class DatabaseFirebaseRecuperar {

    companion object {

        val BancoFirestore = FirebaseFirestore.getInstance()

        @Suppress("unchecked_cast")
        fun recuperarLojasLocais(Cidade:String, Interface:DatabaseLocaisInterface){
            BancoFirestore.collection(Chaves.CHAVE_LOJA.valor).whereEqualTo(Chaves.CHAVE_CIDADE.valor, Cidade).addSnapshotListener { querySnapshot, _ ->
                if(querySnapshot?.documents?.isNotEmpty()!!){
                    val ListaFinal = mutableListOf<Lojas>()
                    for(Item in querySnapshot){
                        Log.i("Teste", Item.toString())
                        ListaFinal.add(Lojas(
                            titulo =  Item["titulo"].toString(),
                            status = Item["status"] as List<String>,
                            imagem = Item["imagem"].toString(),
                            latitude = Item["latitude"] as Double,
                            longitude = Item["longitude"] as Double,
                            local = Item["local"].toString(),
                            fila = Item["fila"] as List<Int>,
                            cidade = Item["cidade"].toString(),
                            telefone = Item["telefone"].toString(),
                            horarios = Item["horarios"] as List<String>,
                            avaliacoes = BigDecimal(Item["avaliacoes"].toString())
                            //avaliacoesRating = BigDecimal(Item["avaliacoesRating"].toString()),
                            //mediaRating = Item["mediaRating"] as Float
                        ))
                    }
                    Interface.dadosRecebidos(ListaFinal)
                }
            }
        }

        fun recuperaDadosPessoa(Email:String, Interface:DatabasePessoaInterface){
            BancoFirestore.collection(Chaves.CHAVE_USUARIO.valor).document(Email).addSnapshotListener { documentSnapshot, _ ->
                if(documentSnapshot?.exists()!!){
                    Log.i("Debug", documentSnapshot.toString())
                    val Item = Perfil(
                        titulo = documentSnapshot["titulo"] as String,
                        email = documentSnapshot["email"] as String,
                        raio = documentSnapshot["raio"].toString().toLong(),
                        pontosCadastro = documentSnapshot["pontosCadastro"].toString().toLong(),
                        pontosFila = documentSnapshot["pontosFila"].toString().toLong(),
                        pontosLocais = documentSnapshot["pontosLocais"].toString().toLong(),
                        pontosTotais = documentSnapshot["pontosTotais"].toString().toLong(),
                        nascimento = documentSnapshot["nascimento"] as String,
                        tipo = documentSnapshot["tipo"] as String
                    )
                    Interface.pessoaRecebida(Item)
                }
            }
        }

        fun recuperarTopRanking(Interface:DatabaseRakingInterface){
            BancoFirestore.collection(Chaves.CHAVE_USUARIO.valor).orderBy(TipoPontos.PONTOS_TOTAIS.valor, Query.Direction.DESCENDING).limit(5).get().addOnCompleteListener {
                if(it.isSuccessful){
                    val ListaFinal = mutableListOf<Perfil>()
                    for(Item in it.result?.documents!!){
                        ListaFinal.add(Perfil(
                            titulo =  Item["titulo"].toString(),
                            pontosTotais = Item["pontosTotais"].toString().toLong(),
                            email = Item["email"].toString()
                        ))
                    }
                    Interface.topRanking(ListaFinal)
                }
            }
        }
    }
}

interface DatabaseLocaisInterface{

    fun dadosRecebidos(Lista:MutableList<Lojas>)

}

interface DatabasePessoaInterface{

    fun pessoaRecebida(Pessoa:Perfil)

}

interface DatabaseRakingInterface{

    fun topRanking(Lista:List<Perfil>)

}

interface DatabaseMudanca{

    fun Resposta(Resposta:Respostas)

}