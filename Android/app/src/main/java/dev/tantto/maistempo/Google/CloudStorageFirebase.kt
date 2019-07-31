package dev.tantto.maistempo.Google

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.io.File

enum class TipoDonwload(var Valor:String){
    ICONE("iconesLojas"),
    PERFIl("images")
}

class CloudStorageFirebase {

    companion object {
        private val Store = FirebaseStorage.getInstance()

        fun salvarFotoCloud(Caminho:Uri?, Email:String, Interface:EnviarFotoCloud){
            val Referecia = Store.reference.child("images/$Email")
            if (Caminho != null){
                Referecia.putFile(Caminho).addOnCompleteListener{
                    Interface.enviadaSucesso()

                }.addOnFailureListener {
                    Interface.falhaEnviar(it.localizedMessage)

                }.addOnProgressListener {
                    val Progresso = 100.0 * it.bytesTransferred / it.totalByteCount
                    Interface.enviarProgresso(Progresso)
                }
            } else {
                Interface.falhaEnviar("ERRO")
            }
        }

        fun mudarImagem(Caminho: Uri, Email: String) : UploadTask{
            return Store.reference.child("images/$Email").putFile(Caminho)
        }

        fun deletarImagem(Email: String){
            Store.reference.child("images/$Email").delete()
        }

    }

    fun donwloadCloud(Email: String, Tipo:TipoDonwload, Interface:DownloadFotoCloud){
        /*val Refencia = Store.getReference("${Tipo.Valor}/$Email")
        val arquivo = File.createTempFile("perfil", "jpg")
        Refencia.getFile(arquivo).addOnCompleteListener {
            if(it.isSuccessful){
                val image = BitmapFactory.decodeFile(arquivo.absolutePath)
                Interface.imagemBaixada(image)
                arquivo.delete()
            }
        }*/
        Interface.imagemBaixada(null)
    }

}

interface EnviarFotoCloud{

    fun enviadaSucesso()
    fun falhaEnviar(Erro:String)
    fun enviarProgresso(Progresso:Double)

}

interface DownloadFotoCloud{

    fun imagemBaixada(Imagem:Bitmap?)

}