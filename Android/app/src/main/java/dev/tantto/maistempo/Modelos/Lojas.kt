package dev.tantto.maistempo.Modelos

import java.io.Serializable

data class Lojas(
    var id:String,
    var titulo:String,
    var status:List<String>,
    var imagem:String,
    var latitude:Double,
    var longitude:Double,
    var local:String,
    var filaNormal:List<Int>,
    var filaRapida:List<Int>,
    var filaPreferencial:List<Int>,
    var cidade:String,
    var telefone:String,
    var horarios:List<String>,
    var quantidadeAvaliacoesFila: Int,
    var quantidadeAvaliacoesRating: Int,
    var mediaRating:Float = 0.0F
) : Serializable