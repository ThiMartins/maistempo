package dev.tantto.maistempo.Servicos

import android.graphics.Bitmap
import android.os.AsyncTask
import dev.tantto.maistempo.Classes.bitmapUtils
import dev.tantto.maistempo.Modelos.Lojas
import java.io.IOException

class baixarImagemAsyncTask : AsyncTask<String, Int, Bitmap>() {

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


interface LojasRecuperadas{

    fun DadosRecebidos(Lista: MutableList<Lojas>)

}