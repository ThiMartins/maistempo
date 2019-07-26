package dev.tantto.maistempo.Classes

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.URL

class bitmapUtils {

    companion object {

        @Throws(IOException::class)
        fun baixarImagem(Url:String) : Bitmap {
            val endereco = URL(Url)
            val recuperarStream = endereco.openStream()
            val imagem = BitmapFactory.decodeStream(recuperarStream)
            recuperarStream.close()
            return imagem
        }

        fun getImageUri(ImagemBitmap: Bitmap, Email:String, Contexto:Context): Uri {
            val bytes = ByteArrayOutputStream()
            ImagemBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val path = MediaStore.Images.Media.insertImage(Contexto.contentResolver, ImagemBitmap, Email, null)
            return Uri.parse(path)
        }

    }

}