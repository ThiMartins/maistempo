package dev.tantto.maistempo.Modelos

import java.io.Serializable

data class Perfil(
    var titulo:String = "User",
    var imagem:String = "",
    var email:String = "email",
    var raio:Long = 1,
    var pontosCadastro:Long = 1,
    var pontosFila:Long = 0,
    var pontosLocais:Long = 0,
    var pontosTotais:Long = 0,
    var nascimento:String = "",
    var senha:String = "",
    var favoritos:MutableList<String> = mutableListOf()
) : Serializable