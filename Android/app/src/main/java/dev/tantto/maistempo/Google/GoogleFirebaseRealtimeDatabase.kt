package dev.tantto.maistempo.Google

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import dev.tantto.maistempo.Modelos.Perfil

class GoogleFirebaseRealtimeDatabase {

    companion object {

        val BancoFirestore = FirebaseFirestore.getInstance()
        val BancoDatabase = FirebaseDatabase.getInstance()

        fun SalvarDados(Dados:Perfil){
            val valores = BancoFirestore.collection("usuarios").document(Dados.Email).set(Dados)
        }

        fun RecuperarDados(Email:String, IntefaceRecebida:GoogleRealtimeInterface){
            val ResultadoBanco = BancoFirestore.collection("usuarios").document(Email).get(Source.CACHE).addOnCompleteListener {
                val Resultado = it.result!!.toObject(Perfil::class.java)
                if(Resultado != null){
                    IntefaceRecebida.DadosRecebidos(Resultado)
                }
            }
        }

    }

}

interface GoogleRealtimeInterface {

    fun DadosRecebidos(Dados: Perfil)

}