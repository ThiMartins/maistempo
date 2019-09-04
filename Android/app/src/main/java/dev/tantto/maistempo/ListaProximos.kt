package dev.tantto.maistempo

import com.firebase.geofire.GeoLocation

class ListaProximos {

    companion object {

         var Lista:MutableMap<String, GeoLocation> = mutableMapOf()

        fun adicionar(Id:String, Local:GeoLocation){
            if(!Lista.contains(Id)){
                Lista[Id] = Local
            }
        }

        fun recuperar(Chave:String) : GeoLocation{
            return Lista[Chave]!!
        }

        fun limpar(){
            Lista.clear()
        }

        fun contem(Id: String): Boolean {
            return Lista.containsKey(Id)
        }

        fun tamanho() : Int{
            return Lista.size
        }

    }

}