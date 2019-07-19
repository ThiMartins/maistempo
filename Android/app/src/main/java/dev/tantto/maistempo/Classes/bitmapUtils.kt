package dev.tantto.maistempo.Classes

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
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
    }

}