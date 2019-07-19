package dev.tantto.maistempo.Google

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import dev.tantto.maistempo.Chaves.Chaves
import dev.tantto.maistempo.Modelos.Lojas
import dev.tantto.maistempo.Modelos.Perfil

class DatabaseFirebaseSalvar {

    companion object {

        val BancoFirestore = FirebaseFirestore.getInstance()
        val BancoDatabase = FirebaseDatabase.getInstance()

        fun SalvarDados(Dados:Perfil){
            BancoFirestore.collection("usuarios").document(Dados.Email).set(Dados)
        }

        fun AdicionarDadosLocal(){

        }
    }

}

class DatabaseFirebaseRecuperar {

    companion object {

        val BancoFirestore = FirebaseFirestore.getInstance()

        fun RecuperarDados(Email:String, User:FirebaseUser?, intefaceRecebida:DatabasePerfilInterface){
            BancoFirestore.collection(Chaves.CHAVE_USUARIO.valor).document(Email).get(Source.CACHE).addOnCompleteListener {
                val Resultado = it.result!!.toObject(Perfil::class.java)
                if(Resultado != null){
                    intefaceRecebida.DadosRecebidos(User,Resultado)
                }
            }
        }

        @Suppress("unchecked_cast")
        fun RecuperarLocal(Cidade:String, Interface:DatabaseLocaisInterface){
            BancoFirestore.collection(Chaves.CHAVE_LOJA.valor).whereEqualTo(Chaves.CHAVE_CIDADE.valor, Cidade).addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if(querySnapshot?.documents?.isNotEmpty()!!){
                    val ListaFinal = mutableListOf<Lojas>()
                    for(Item in querySnapshot){
                        ListaFinal.add(Lojas(
                            Item["titulo"] as String,
                            Item["status"] as List<String>,
                            Item["imagem"] as String,
                            Item["latitude"] as Double,
                            Item["longitude"] as Double,
                            Item["local"] as String,
                            Item["fila"] as List<String>,
                            Item["cidade"] as String
                        ))
                    }
                    Interface.DadosRecebidos(ListaFinal)
                }
            }
        }
    }
}

interface DatabasePerfilInterface {

    fun DadosRecebidos(User: FirebaseUser?, Dados: Perfil)

}

interface DatabaseLocaisInterface{

    fun DadosRecebidos(Lista:MutableList<Lojas>)

}