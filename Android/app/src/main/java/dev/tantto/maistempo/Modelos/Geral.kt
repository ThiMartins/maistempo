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
    var Modo:Tipo,
    var PontosCadastro:Int = 0,
    var PontosFila:Int = 0,
    var PontosLocais:Int = 0,
    var PontosTotais:Int = 0
)