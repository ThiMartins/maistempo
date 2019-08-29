package dev.tantto.maistempo.modelos


class Geral(var Titulo:String, var Imagem:Int, var Modo:Tipo) { enum class Tipo{
        GERAL,
        RANKING,
        TERMOS,
        SAIR
    }

}