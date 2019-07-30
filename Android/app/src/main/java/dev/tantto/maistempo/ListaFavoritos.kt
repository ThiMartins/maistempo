package dev.tantto.maistempo

import dev.tantto.maistempo.Modelos.Lojas

class ListaFavoritos {

    companion object {
        var Lista = mutableListOf<String>()

        fun recuperarFavoritos() : MutableList<Lojas>{
            return if(Lista.isNotEmpty()){
                val novaLista = ListaLocais.recuperarTudo().filterIndexed { index, lojas ->
                    Lista[index] == lojas.id
                } as MutableList<Lojas>
                novaLista
            } else {
                mutableListOf()
            }
        }
    }

}