package dev.tantto.maistempo.classes

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationListener
import android.location.LocationManager
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil

@SuppressLint("MissingPermission")
class LocalizacaoPessoa(Contexto:Context, Provider:String, TempoMinimo:Long, DistanciaMinima:Float, private val Interface:LocationListener) {

    private var LocalPessoa:LocationManager = Contexto.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    init {
        LocalPessoa.requestLocationUpdates(Provider, TempoMinimo, DistanciaMinima, Interface)
    }

    fun removerChamada(){
        LocalPessoa.removeUpdates(Interface)
    }

    companion object {

        fun providerAtivo(Contexto: Context , Tipo:String) : Boolean{
            return (Contexto.getSystemService(Context.LOCATION_SERVICE) as LocationManager).isProviderEnabled(Tipo)
        }

        fun calcularDistancia(De: LatLng, Para:LatLng) : Double {
            return SphericalUtil.computeDistanceBetween(De, Para)
        }

    }

}