package dev.tantto.maistempo.classes

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import java.io.ByteArrayOutputStream
import java.io.IOException

class BitmapUtilitarios {

    companion object {

        /*@Throws(IOException::class)
        fun baixarImagem(Url:String) : Bitmap {
            val endereco = URL(Url)
            val recuperarStream = endereco.openStream()
            val imagem = BitmapFactory.decodeStream(recuperarStream)
            recuperarStream.close()
            return imagem
        }*/

        @Throws(IOException::class)
        fun getImageUri(ImagemBitmap: Bitmap, Email:String, Contexto:Context): Uri {
            val bytes = ByteArrayOutputStream()
            ImagemBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val path = MediaStore.Images.Media.insertImage(Contexto.contentResolver, ImagemBitmap, Email, null)
            return Uri.parse(path)
        }

    }

}