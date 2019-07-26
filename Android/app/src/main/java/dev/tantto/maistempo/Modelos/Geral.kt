package dev.tantto.maistempo.Modelos

enum class Tipo{
    GERAL,
    RANKING,
    TERMOS,
    SAIR
}

class Geral(
    var Titulo:String,
    var Imagem:Int,
    var Modo:Tipo
)