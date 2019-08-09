package dev.tantto.maistempo

import dev.tantto.maistempo.modelos.Lojas

class ListaLocais {

    companion object {

        private var ListaFavoritos = mutableListOf<String>()
        private var BackupFavortios = mutableListOf<String>()
        private var BackupLojas:MutableList<Lojas>? = mutableListOf()
        private var ListaLojas = mutableListOf<Lojas>()

        fun adicionarFavorito(Valor: String){
            ListaFavoritos.add(Valor)
            BackupFavortios = ListaFavoritos
        }

        fun removerFavoritos(Valor: String){
            ListaFavoritos.remove(Valor)
            BackupFavortios = ListaFavoritos
        }

        fun refazer(Tabela:MutableList<Lojas>){
            ListaLojas = Tabela
            BackupLojas = ListaLojas
        }

        fun refazerFavoritos(Tabela:MutableList<String>) {
            ListaFavoritos = Tabela
            BackupFavortios = ListaFavoritos
        }

        fun recuperarTudo() : MutableList<Lojas>{
            return ListaLojas
        }

        fun recuperar(Index:Int): Lojas {
            return ListaLojas[Index]
        }

        fun tamanho() : Int{
            return ListaLojas.size
        }

        fun contemFavoritos(Valor:String?): Boolean{
            return ListaFavoritos.contains(Valor)
        }

        fun filto(Filtragem:String){
            ListaLojas =
                (if(Filtragem.isNotEmpty()){
                    ListaLojas.filter { it.titulo.toLowerCase().contains(Filtragem.toLowerCase()) }
                } else{
                    BackupLojas!!
                }) as MutableList<Lojas>
        }

        @Suppress("UNCHECKED_CAST")
        fun filtroFavoritos(Filtragem:String){
            ListaFavoritos =
                (if(Filtragem.isNotEmpty()){
                    ListaFavoritos.filter { it.contains(Filtragem) }
                } else{
                    BackupLojas!!
                }) as MutableList<String>
        }

        fun recuperarFavorito(Index: Int) : Lojas?{
            return if(ListaFavoritos.isNotEmpty()){
                ListaLojas.first {
                    it.id == ListaFavoritos[Index]
                }
            } else {
                null
            }
        }

        fun tamanhoFavoritos() :Int {
            return ListaFavoritos.size
        }

    }

}