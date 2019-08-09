package dev.tantto.maistempo.modelos

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