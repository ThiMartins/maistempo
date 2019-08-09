package dev.tantto.maistempo.modelos

import java.io.Serializable

data class Lojas(
    var id:String,
    var titulo:String,
    var latitude:Double,
    var longitude:Double,
    var local:String,
    var filaNormal:HashMap<String, Double>,
    var filaRapida:HashMap<String, Double>,
    var filaPreferencial:HashMap<String, Double>,
    var cidade:String,
    var telefone:String,
    var quantidadeAvaliacoesFila: Int,
    var quantidadeAvaliacoesRating: Int,
    var horarioInicio:Int,
    var horarioFinal:Int,
    var mediaRanking:Double
) : Serializable