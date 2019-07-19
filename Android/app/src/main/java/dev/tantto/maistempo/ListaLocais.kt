package dev.tantto.maistempo

import dev.tantto.maistempo.Modelos.Lojas

class ListaLocais {

    companion object {
        private var Lista = mutableListOf<Lojas>()

        fun Refazer(Tabela:MutableList<Lojas>){
            Lista = Tabela
        }

        fun Adicionar(Loja:Lojas){
            Lista.add(Loja)
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

}