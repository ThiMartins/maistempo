package dev.tantto.maistempo.Google

import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import dev.tantto.maistempo.Modelos.Lojas
import dev.tantto.maistempo.Modelos.Perfil

class GoogleFirebaseRealtimeDatabase {

    companion object {

        val BancoFirestore = FirebaseFirestore.getInstance()
        val BancoDatabase = FirebaseDatabase.getInstance()

        fun SalvarDados(Dados:Perfil){
            BancoFirestore.collection("usuarios").document(Dados.Email).set(Dados)
        }

        fun RecuperarDados(Email:String, User:FirebaseUser?,IntefaceRecebida:GoogleRealtimeInterface){
            val ResultadoBanco = BancoFirestore.collection("usuarios").document(Email).get(Source.CACHE).addOnCompleteListener {
                val Resultado = it.result!!.toObject(Perfil::class.java)
                if(Resultado != null){
                    IntefaceRecebida.DadosRecebidos(User,Resultado)
                }
            }
        }

        fun RecuperarDadosLocal(Min:Double, Max:Double, Interface:Recuperados){
            val Referencia = BancoFirestore.collection("lojas")
            Referencia.whereGreaterThanOrEqualTo("longitude", Min).whereLessThanOrEqualTo("longitude", Max).addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if(querySnapshot?.documents?.isNotEmpty()!!){
                    //val Resultados = querySnapshot.toObjects(Lojas::class.java)
                    //I
                    var ListaRecebida = listOf<Lojas>()
                    for(Item in querySnapshot){
                        ListaRecebida += Lojas(
                            Item["titulo"] as String,
                            Item["status"] as List<String>,
                            Item["imagem"] as String,
                            Item["latitude"] as Long,
                            Item["longitude"] as Long,
                            Item["local"] as String,
                            Item["fila"] as List<String>
                        )
                    }
                    Interface.Recuperado(ListaRecebida)
                } else{
                    Interface.Recuperado(listOf())
                }
            }
        }

        fun AdicionarDadosLocal(){

        }
    }

}

interface GoogleRealtimeInterface {

    fun DadosRecebidos(User: FirebaseUser?, Dados: Perfil)

}

interface Recuperados{

    fun Recuperado(Lista:List<Lojas>)

}