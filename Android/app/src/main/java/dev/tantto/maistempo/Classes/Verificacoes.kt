package dev.tantto.maistempo.classes

import android.content.Context
import android.net.ConnectivityManager

class VerificarInternet {

    companion object {

        @Suppress("DEPRECATION")
        fun internetConectado(Contexto:Context) : Boolean? {
            val ConexaoManager = Contexto.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return ConexaoManager.activeNetworkInfo?.isConnected
        }

    }

}