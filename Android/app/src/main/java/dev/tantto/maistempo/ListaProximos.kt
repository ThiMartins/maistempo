package dev.tantto.maistempo

import com.firebase.geofire.GeoLocation

class ListaProximos {

    companion object {

        private var Lista:MutableMap<String, GeoLocation> = mutableMapOf()

        fun adicionar(Id:String, Local:GeoLocation){
            if(!Lista.contains(Id)){
                Lista.put(Id, Local)
            }
        }

        fun recuperar(Chave:String) : GeoLocation{
            return Lista[Chave]!!
        }

        fun recuperarChave() : List<String>{
            return Lista.keys.toList()
        }

        fun recuperarValores() : List<GeoLocation> {
            return Lista.values.toList()
        }

        fun recuperarTudo() : MutableMap<String, GeoLocation>{
            return Lista
        }

    }

}