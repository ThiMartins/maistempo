package dev.tantto.maistempo.Google

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
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

        fun SalvarFotoCloud(Caminho:Uri?, Email:String,Interface:EnviarFotoCloud){
            val Referecia = Store.reference.child("images/$Email")
            if (Caminho != null){
                Referecia.putFile(Caminho).addOnCompleteListener{
                    Interface.EnviadaSucesso()

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

        fun mudarImagem(Caminho: Uri, Email: String) : UploadTask{
            return Store.reference.child("images/$Email").putFile(Caminho)
        }

    }

    fun DonwloadCloud(Email: String, Tipo:TipoDonwload, Interface:DownloadFotoCloud){
        val Refencia = Store.getReference("${Tipo.Valor}/$Email")
        val arquivo = File.createTempFile("perfil", "jpg")
        Refencia.getFile(arquivo).addOnCompleteListener {
            if(it.isSuccessful){
                val image = BitmapFactory.decodeFile(arquivo.absolutePath)
                Interface.ImagemBaixada(image)
                Log.i("Debug", "Finalizado")
            } else {
                Log.i("Debug", "Erro")
            }
        }
    }

}

interface EnviarFotoCloud{

    fun EnviadaSucesso()
    fun FalhaEnviar(Erro:String)
    fun EnviarProgresso(Progresso:Double)

}

interface DownloadFotoCloud{

    fun ImagemBaixada(Imagem:Bitmap)

}