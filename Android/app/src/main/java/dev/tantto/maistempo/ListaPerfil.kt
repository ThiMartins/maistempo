package dev.tantto.maistempo

import dev.tantto.maistempo.Modelos.Geral
import dev.tantto.maistempo.Modelos.Tipo

class ListaPerfil {

    private var lista:MutableList<Geral> = mutableListOf(
        Geral("Geral", R.drawable.setting_dark, Tipo.GERAL),
        Geral("Ranking", R.drawable.star_black, Tipo.RANKING),
        Geral("Termos de Utilixação", R.drawable.termos_dark, Tipo.TERMOS),
        Geral("Sair", R.drawable.sair_dark, Tipo.SAIR)
    )

    fun RecuperarTudo() : MutableList<Geral>{
        return lista
    }

    fun Recuperar(Index:Int) : Geral {
        return lista[Index]
    }

}