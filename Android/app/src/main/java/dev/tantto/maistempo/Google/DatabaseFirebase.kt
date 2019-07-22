package dev.tantto.maistempo.Google

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dev.tantto.maistempo.Chaves.Chaves
import dev.tantto.maistempo.Modelos.Lojas
import dev.tantto.maistempo.Modelos.Perfil
import java.math.BigDecimal

class DatabaseFirebaseSalvar {

    companion object {

        val BancoFirestore = FirebaseFirestore.getInstance()
        val BancoDatabase = FirebaseDatabase.getInstance()

        fun SalvarDados(Dados:Perfil){
            BancoFirestore.collection("usuarios").document(Dados.email).set(Dados)
        }

        fun AdicionarDadosLocal(){

        }
    }

}

class DatabaseFirebaseRecuperar {

    companion object {

        val BancoFirestore = FirebaseFirestore.getInstance()

        @Suppress("unchecked_cast")
        fun RecuperarLocal(Cidade:String, Interface:DatabaseLocaisInterface){
            BancoFirestore.collection(Chaves.CHAVE_LOJA.valor).whereEqualTo(Chaves.CHAVE_CIDADE.valor, Cidade).addSnapshotListener { querySnapshot, _ ->
                if(querySnapshot?.documents?.isNotEmpty()!!){
                    val ListaFinal = mutableListOf<Lojas>()
                    for(Item in querySnapshot){
                        ListaFinal.add(Lojas(
                            titulo =  Item["titulo"] as String,
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
                    Interface.DadosRecebidos(ListaFinal)
                }
            }
        }

        fun RecuperaDadosPessoa(Email:String, Interface:DatabasePessoaInterface){
            BancoFirestore.collection(Chaves.CHAVE_USUARIO.valor).document(Email).addSnapshotListener { documentSnapshot, _ ->
                if(documentSnapshot?.exists()!!){
                    Log.i("Debug", documentSnapshot.toString())
                    val Item = Perfil(
                        titulo = documentSnapshot["titulo"] as String,
                        email = documentSnapshot["email"] as String,
                        raio = documentSnapshot["raio"].toString(),
                        pontosCadastro = documentSnapshot["pontosCadastro"].toString(),
                        pontosFila = documentSnapshot["pontosFila"].toString(),
                        pontosLocais = documentSnapshot["pontosLocais"].toString(),
                        pontosTotais = documentSnapshot["pontosTotais"].toString(),
                        nascimento = documentSnapshot["nascimento"] as String,
                        tipo = documentSnapshot["tipo"] as String
                    )
                    Interface.PessoaRecebida(Item)
                }
            }
        }

        fun RecuperarTopRanking(Interface:DatabaseRakingInterface){
            Log.i("Debug", "Passou")
            BancoFirestore.collection("usuarios").orderBy("pontosTotais", Query.Direction.DESCENDING).limit(5).get().addOnCompleteListener {
                Log.i("Debug", "Passou")
                if(it.isSuccessful){
                    val ListaFinal = mutableListOf<Perfil>()
                    for(Item in it.result?.documents!!){
                        ListaFinal.add(Perfil(
                            titulo =  Item["titulo"].toString(),
                            pontosTotais = Item["pontosTotais"].toString(),
                            email = Item["email"].toString()
                        ))
                    }
                    Interface.TopRanking(ListaFinal)
                }
                Log.i("Debug", it.result?.documents?.get(0)?.toString())
            }
        }
    }
}

interface DatabaseLocaisInterface{

    fun DadosRecebidos(Lista:MutableList<Lojas>)

}

interface DatabasePessoaInterface{

    fun PessoaRecebida(Pessoa:Perfil)

}

interface DatabaseRakingInterface{

    fun TopRanking(Lista:List<Perfil>)

}