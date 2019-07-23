package dev.tantto.maistempo.Google

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

enum class TipoPontos(val valor:String){
    PONTOS_FILA("pontosFila"),
    PONTOS_AVALIACAO("pontosLocais"),
    PONTOS_TOTAIS("pontosTotais")
}

class DatabaseFirebaseSalvar {

    companion object {

        val BancoFirestore = FirebaseFirestore.getInstance()
        val BancoDatabase = FirebaseDatabase.getInstance()

        fun salvarDados(Dados:Perfil){
            BancoFirestore.collection(Chaves.CHAVE_USUARIO.valor).document(Dados.email).set(Dados)
        }

        fun adicionarPontos(Email: String, Pontos:Int, Tipo:TipoPontos){
            val Documento = BancoFirestore.collection(Chaves.CHAVE_USUARIO.valor).document(Email).get()
            Documento.addOnSuccessListener {
                val valorRecuperado = it.get(Tipo.valor).toString().toLong()
                val fila = it.get(TipoPontos.PONTOS_FILA.valor).toString().toLong()
                val avaliacao = it.get(TipoPontos.PONTOS_AVALIACAO.valor).toString().toLong()
                BancoFirestore.collection(Chaves.CHAVE_USUARIO.valor).document(Email).update(Tipo.valor, Pontos + valorRecuperado)
                BancoFirestore.collection(Chaves.CHAVE_USUARIO.valor).document(Email).update(TipoPontos.PONTOS_TOTAIS.valor, fila + avaliacao + 1)
            }

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
                        ListaFinal.add(Lojas(
                            titulo =  Item["titulo"].toString(),
                            status = Item["status"] as List<String>,
                            imagem = Item["imagem"] as String,
                            latitude = Item["latitude"] as Double,
                            longitude = Item["longitude"] as Double,
                            local = Item["local"] as String,
                            fila = Item["fila"] as List<Int>,
                            cidade = Item["cidade"] as String,
                            telefone = Item["telefone"] as String,
                            horarios = Item["horarios"] as List<String>,
                            avaliacoes = BigDecimal(Item["avaliacoes"].toString())
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