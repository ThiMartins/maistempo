package dev.tantto.maistempo

import dev.tantto.maistempo.Modelos.Lojas

class ListaLocais {

    companion object {
        private var Lista:List<Lojas> = listOf()

        fun Refazer(Tabela:List<Lojas>){
            Lista = Tabela
        }

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

}