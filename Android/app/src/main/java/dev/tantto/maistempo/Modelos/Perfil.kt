package dev.tantto.maistempo.Modelos

import android.graphics.Bitmap
import java.io.Serializable

data class Perfil(
    var titulo:String = "User",
    var imagem:Bitmap? = null,
    var email:String = "email",
    var raio:String = "1",
    var pontosCadastro:String = "0",
    var pontosFila:String = "0",
    var pontosLocais:String = "0",
    var pontosTotais:String = "0",
    var nascimento:String = "",
    var tipo:String = "admin",
    var senha:String = ""
) : Serializable