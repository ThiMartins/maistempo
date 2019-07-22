package dev.tantto.maistempo.Modelos

import java.io.Serializable
import java.math.BigDecimal

data class Lojas(
    var titulo:String,
    var status:List<String>,
    var imagem:String,
    var latitude:Double,
    var longitude:Double,
    var local:String,
    var fila:List<Int>,
    var cidade:String,
    var telefone:String,
    var horarios:List<String>,
    var avaliacoes:BigDecimal
) : Serializable