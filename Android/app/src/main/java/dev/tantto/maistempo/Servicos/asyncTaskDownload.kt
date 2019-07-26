package dev.tantto.maistempo.Servicos

import android.graphics.Bitmap
import android.os.AsyncTask
import dev.tantto.maistempo.Classes.bitmapUtils
import java.io.IOException

class baixarImagem : AsyncTask<String, Int, Bitmap>() {

    override fun doInBackground(vararg params: String?): Bitmap? {
        var imagem:Bitmap? = null
        try {
            if (params[0] != null){
                imagem = bitmapUtils.baixarImagem(params[0]!!)
            }
        } catch (Erro:IOException){

        }
        return imagem
    }
}