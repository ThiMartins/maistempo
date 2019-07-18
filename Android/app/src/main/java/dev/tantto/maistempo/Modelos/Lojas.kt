package dev.tantto.maistempo.Modelos

import java.io.Serializable

data class Lojas(
    var titulo:String,
    var status:List<String>,
    var imagem:String,
    var latitude:Long,
    var longitude:Long,
    var Local:String,
    var fila:List<String>
) : Serializable