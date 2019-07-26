package dev.tantto.maistempo.Google

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dev.tantto.maistempo.Chaves.Chaves
import dev.tantto.maistempo.Modelos.Lojas
import dev.tantto.maistempo.Modelos.Perfil
import java.math.BigDecimal

enum class TipoPontos(val valor:String){
    PONTOS_FILA("pontosFila"),
    PONTOS_AVALIACAO("pontosLocais"),
    PONTOS_TOTAIS("pontosTotais")
}

enum class Respostas(valor: String){
    SUCESSO("sucesso"),
    ERRO("erro")
}

class DatabaseFirebaseSalvar {

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

        @Suppress("UNCHECKED_CAST")
        fun adicionarFavorito(Email: String, LojaRecebida:String){
            FirebaseFirestore.getInstance().collection(Chaves.CHAVE_USUARIO.valor).document(Email).get().addOnCompleteListener {
                if(it.isSuccessful){
                    val Recuperado = it.result?.get(Chaves.CHAVE_FAVORITOS.valor)
                    if(Recuperado != null){
                        val lojas = Recuperado as MutableList<String>
                        lojas.add(LojaRecebida)
                        FirebaseFirestore.getInstance().collection(Chaves.CHAVE_USUARIO.valor).document(Email).update(Chaves.CHAVE_FAVORITOS.valor, lojas)

                    } else {
                        val lista = mutableListOf(LojaRecebida)
                        FirebaseFirestore.getInstance().collection(Chaves.CHAVE_USUARIO.valor).document(Email).update(Chaves.CHAVE_FAVORITOS.valor, lista)
                    }
                }
            }
        }

        @Suppress("UNCHECKED_CAST")
        fun removerFavorito(Email: String, LojaRecebida:String){

        }

        fun mudarRaio(Email: String, Valor: Int){
            FirebaseFirestore.getInstance().collection(Chaves.CHAVE_USUARIO.valor).document(Email).update(Chaves.CHAVE_RAIO.valor, Valor)
        }

        fun mudarNomeComImagem(Email: String, Nome: String = "", Caminho:Uri = Uri.EMPTY, Resposta: DatabaseMudanca){
            if(Nome.isNotEmpty() && Caminho == Uri.EMPTY){
                FirebaseFirestore.getInstance().collection(Chaves.CHAVE_USUARIO.valor).document(Email).update(Chaves.CHAVE_TITULO.valor, Nome).addOnCompleteListener {
                    if(it.isSuccessful){
                        Resposta.resposta(Respostas.SUCESSO)
                    } else {
                        Resposta.resposta(Respostas.ERRO)
                    }
                }
            } else if(Nome.isEmpty() && Caminho != Uri.EMPTY){
                CloudStorageFirebase.mudarImagem(Caminho, Email).addOnCompleteListener {
                    if(it.isSuccessful){
                        Resposta.resposta(Respostas.SUCESSO)
                    } else {
                        Resposta.resposta(Respostas.ERRO)
                    }
                }
            } else {
                FirebaseFirestore.getInstance().collection(Chaves.CHAVE_USUARIO.valor).document(Email).update(Chaves.CHAVE_TITULO.valor, Nome).addOnCompleteListener { it ->
                    if(it.isSuccessful){
                        Resposta.resposta(Respostas.SUCESSO)
                        CloudStorageFirebase.mudarImagem(Caminho, Email).addOnCompleteListener {
                            if(it.isSuccessful){
                                Resposta.resposta(Respostas.SUCESSO)
                            } else {
                                Resposta.resposta(Respostas.ERRO)
                            }
                        }
                    } else {
                        Resposta.resposta(Respostas.ERRO)
                    }
                }
            }
        }

        fun deletarConta(Email: String){
            FirebaseFirestore.getInstance().collection(Chaves.CHAVE_USUARIO.valor).document(Email).delete()
            CloudStorageFirebase.deletarImagem(Email)
        }
    }

}

class DatabaseFirebaseRecuperar {

    companion object {

        @Suppress("unchecked_cast")
        fun recuperarLojasLocais(Cidade:String, Interface:DatabaseLocaisInterface){
            FirebaseFirestore.getInstance().collection(Chaves.CHAVE_LOJA.valor).whereEqualTo(Chaves.CHAVE_CIDADE.valor, Cidade).addSnapshotListener { querySnapshot, _ ->
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
            FirebaseFirestore.getInstance().collection(Chaves.CHAVE_USUARIO.valor).document(Email).addSnapshotListener { documentSnapshot, _ ->
                if(documentSnapshot?.exists()!!){
                    Log.i("Debug", documentSnapshot.toString())
                    val Item = Perfil(
                        titulo = documentSnapshot["titulo"].toString(),
                        email = documentSnapshot["email"].toString(),
                        raio = documentSnapshot["raio"].toString().toLong(),
                        pontosCadastro = documentSnapshot["pontosCadastro"].toString().toLong(),
                        pontosFila = documentSnapshot["pontosFila"].toString().toLong(),
                        pontosLocais = documentSnapshot["pontosLocais"].toString().toLong(),
                        pontosTotais = documentSnapshot["pontosTotais"].toString().toLong(),
                        nascimento = documentSnapshot["nascimento"].toString(),
                        tipo = documentSnapshot["tipo"].toString()
                    )
                    Interface.pessoaRecebida(Item)
                }
            }
        }

        fun recuperarTopRanking(Interface:DatabaseRakingInterface){
            FirebaseFirestore.getInstance().collection(Chaves.CHAVE_USUARIO.valor).orderBy(TipoPontos.PONTOS_TOTAIS.valor, Query.Direction.DESCENDING).limit(5).get().addOnCompleteListener {
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

    fun resposta(Resposta:Respostas)

}