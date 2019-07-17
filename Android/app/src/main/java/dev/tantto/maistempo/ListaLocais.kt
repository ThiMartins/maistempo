package dev.tantto.maistempo

import dev.tantto.maistempo.Modelos.Lojas

class ListaLocais {

    private var Lista:List<Lojas> = listOf(
        Lojas("Extra", "Aberto", null),
        Lojas("Carrefour", "Aberto", null),
        Lojas("Tauste", "Fechado", null),
        Lojas("Dia", "Aberto", null),
        Lojas("Bom Brasil", "Aberto", null),
        Lojas("Galvez", "Fechado", null),
        Lojas("Paulistão", "Aberto", null),
        Lojas("McDonalds", "Aberto", null),
        Lojas("Burguer King", "Fechado", null),
        Lojas("Giraffas", "Aberto", null),
        Lojas("Habbibs", "Aberto", null),
        Lojas("Bar", "Fechado", null)
    )

    fun Adicionar(Loja:Lojas){
        Lista += Loja
    }

    fun Recuperar(Index:Int) : Lojas{
        return Lista[Index]
    }

    fun RecuperarTudo() : List<Lojas>{
        return Lista
    }

    fun Tamanho() : Int{
        return Lista.size
    }

}