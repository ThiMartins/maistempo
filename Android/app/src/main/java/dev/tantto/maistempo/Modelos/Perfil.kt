package dev.tantto.maistempo.Modelos

import android.graphics.Bitmap
import java.io.Serializable

data class Perfil(
    var titulo:String = "User",
    var imagem:Bitmap? = null,
    var email:String = "email",
    var raio:Long = 1,
    var pontosCadastro:Long = 0,
    var pontosFila:Long = 0,
    var pontosLocais:Long = 0,
    var pontosTotais:Long = 0,
    var nascimento:String = "",
    var tipo:String = "admin",
    var senha:String = ""
) : Serializable