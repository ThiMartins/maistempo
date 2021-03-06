package dev.tantto.maistempo.google

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
                    Interface.falhaEnviar(it.localizedMessage!!)

                }.addOnProgressListener {
                    val Progresso = 100.0 * it.bytesTransferred / it.totalByteCount
                    Interface.enviarProgresso(Progresso)
                }
            } else {
                Interface.falhaEnviar("ERRO")
            }
        }

        fun salvarFotoCloud(Caminho:Uri?, Id:String){
            val Referecia = Store.reference.child("iconesLojas/$Id")
            if (Caminho != null){
                Referecia.putFile(Caminho)
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
        val Refencia = Store.getReference("${Tipo.Valor}/$Email")
        val arquivo = File.createTempFile("perfil", "jpg")
        Refencia.getFile(arquivo).addOnCompleteListener {
            if(it.isSuccessful){
                val image = BitmapFactory.decodeFile(arquivo.absolutePath)
                val Imagem = Pair(Email, image)
                Interface.imagemBaixada(hashMapOf(Imagem))
                arquivo.delete()
            } else {
                Interface.imagemBaixada(null)
            }
        }.addOnFailureListener {
            Interface.imagemBaixada(null)
        }
    }

}

interface EnviarFotoCloud{

    fun enviadaSucesso()
    fun falhaEnviar(Erro:String)
    fun enviarProgresso(Progresso:Double)

}

interface DownloadFotoCloud{

    fun imagemBaixada(Imagem:HashMap<String, Bitmap>?)

}