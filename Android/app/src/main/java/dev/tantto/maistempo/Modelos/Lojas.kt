package dev.tantto.maistempo.Modelos

import java.io.Serializable

data class Lojas(
    var titulo:String,
    var status:List<String>,
    var imagem:String,
    var latitude:Double,
    var longitude:Double,
    var Local:String,
    var fila:List<String>,
    var cidade:String
) : Serializable