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

enum class Permissoes(val valor:String){
    CAMERA(Manifest.permission.CAMERA)
    //ARMAZENAMENTO_READ(Manifest.permission.READ_EXTERNAL_STORAGE),
    //ARMAZENAMENTO_WRITE(Manifest.permission.WRITE_EXTERNAL_STORAGE),
}

class Permissao {

    companion object {

        fun veficarPermissao(Contexto:Activity, Tipo:Permissoes) : TipoDePermissao{
            return if(ContextCompat.checkSelfPermission(Contexto, Tipo.valor) != PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.shouldShowRequestPermissionRationale(Contexto, Tipo.valor)){
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