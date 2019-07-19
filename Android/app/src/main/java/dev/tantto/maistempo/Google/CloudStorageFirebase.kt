package dev.tantto.maistempo.Google

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage

class CloudStorageFirebase {

    companion object {
        private val Store = FirebaseStorage.getInstance()

        fun SalvarFotoCloud(Caminho:Uri?, Email:String,Interface:EnviarFotoCloud){
            val Referecia = Store.reference.child("images/$Email")
            if (Caminho != null){
                Referecia.putFile(Caminho).addOnCompleteListener{
                    Interface.EnviadaSucesso(it.result?.uploadSessionUri)

                }.addOnFailureListener {
                    Interface.FalhaEnviar(it.localizedMessage)

                }.addOnProgressListener {
                    val Progresso = 100.0 * it.bytesTransferred / it.totalByteCount
                    Interface.EnviarProgresso(Progresso)
                }
            } else {
                Interface.FalhaEnviar("ERRO")
            }
        }

    }
}

interface EnviarFotoCloud{

    fun EnviadaSucesso(Foto:Uri?)
    fun FalhaEnviar(Erro:String)
    fun EnviarProgresso(Progresso:Double)

}
