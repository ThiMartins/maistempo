package dev.tantto.maistempo.Modelos

import java.io.Serializable

data class Perfil(
    var titulo:String = "User",
    var imagem:String = "",
    var email:String = "email",
    var raio:Long = 100,
    var pontosCadastro:Long = 1,
    var pontosFila:Long = 0,
    var pontosLocais:Long = 0,
    var pontosTotais:Long = 1,
    var nascimento:String = "",
    var senha:String = "",
    var lojasFavoritas:MutableList<String> = mutableListOf(),
    var cidade:String = ""
) : Serializable