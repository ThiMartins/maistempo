package dev.tantto.maistempo

import dev.tantto.maistempo.Modelos.Lojas

class ListaLocais {

    companion object {
        private var Lista = mutableListOf<Lojas>()

        fun refazer(Tabela:MutableList<Lojas>){
            Lista = Tabela
        }

        fun recuperarTudo() : List<Lojas>{
            return Lista
        }
    }

}