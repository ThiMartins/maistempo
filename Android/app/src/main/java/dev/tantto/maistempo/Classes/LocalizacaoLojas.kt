package dev.tantto.maistempo.classes

import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.firebase.geofire.GeoQueryEventListener
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LocalizacaoLojas {

    companion object {

        private val Referencia:DatabaseReference = FirebaseDatabase.getInstance().reference
        private var GeoFireReferencia:GeoFire? = null

        fun adicionarLojas(Nome:String, Localizacao: GeoLocation) {
            GeoFireReferencia = GeoFire(Referencia)
            GeoFireReferencia?.setLocation(Nome, Localizacao) { _, _ ->

            }

        }

        fun buscarLojas(Raio:Double, Localizacao:GeoLocation, Interface:LocalizacaoLoja){
            GeoFireReferencia = GeoFire(Referencia)
            GeoFireReferencia?.queryAtLocation(Localizacao, Raio)?.addGeoQueryEventListener(object : GeoQueryEventListener{
                override fun onGeoQueryReady() {
                    Interface.lojaRecebida(null, null)
                }

                override fun onKeyEntered(key: String?, location: GeoLocation?) {
                    if(key != null && location != null){
                        Interface.lojaRecebida(key, location)
                    } else {
                        Interface.lojaRecebida(null, null)
                    }
                }

                override fun onKeyMoved(key: String?, location: GeoLocation?) {

                }

                override fun onKeyExited(key: String?) {

                }

                override fun onGeoQueryError(error: DatabaseError?) {

                }
            })
        }

    }

    interface LocalizacaoLoja {

        fun lojaRecebida(Id:String?, Localizacao: GeoLocation?)

    }

}