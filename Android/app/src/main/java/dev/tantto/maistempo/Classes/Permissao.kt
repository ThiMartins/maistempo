package dev.tantto.maistempo.Classes

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


enum class TipoDePermissao(val Valor:Int){
    PERMITIDO(0),
    SEM_PERMISSAO(1),
    REPETIR_PEDIDO(2)
}

class Permissao {

    companion object {

        fun veficarPermissao(Contexto:Activity) : TipoDePermissao{
            return if(ContextCompat.checkSelfPermission(Contexto, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.shouldShowRequestPermissionRationale(Contexto, Manifest.permission.CAMERA)){
                    TipoDePermissao.REPETIR_PEDIDO
                } else {
                    TipoDePermissao.SEM_PERMISSAO
                }
            } else {
                TipoDePermissao.PERMITIDO
            }
        }

    }

}