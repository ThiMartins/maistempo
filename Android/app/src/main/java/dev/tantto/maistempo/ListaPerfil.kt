package dev.tantto.maistempo

import dev.tantto.maistempo.modelos.Geral

class ListaPerfil {

    private var lista:MutableList<Geral> = mutableListOf(
        Geral("Geral", R.drawable.setting_dark, Geral.Tipo.GERAL),
        Geral("Ranking", R.drawable.star_black, Geral.Tipo.RANKING),
        Geral("Termos de Utilização", R.drawable.termos_dark, Geral.Tipo.TERMOS),
        Geral("Sair", R.drawable.sair_dark, Geral.Tipo.SAIR)
    )

    fun recuperarTudo() : MutableList<Geral>{
        return lista
    }

    fun recuperar(Index:Int) : Geral {
        return lista[Index]
    }

}