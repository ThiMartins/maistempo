package dev.tantto.maistempo.classes

import android.app.Activity
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import com.firebase.geofire.GeoLocation
import dev.tantto.maistempo.ListaProximos

class BuscarLojasProximas(private val Tela:Activity) : LocationListener {

    fun procurarProximos(){
        //LocalizacaoLojas.adicionarLojas("4HZ2cLSmQFg8BZ72bEmN", GeoLocation(-23.506599, -47.488397))
        //LocalizacaoLojas.adicionarLojas("8IKxe0mCNDoa9qx45gZQ", GeoLocation(-23.506599, -47.488397))

        when {
            Permissao.veficarPermissao(Tela, Permissao.Permissoes.LOCALIZACAO_FINE) == Permissao.TipoDePermissao.PERMITIDO && LocalizacaoPessoa.providerAtivo(Tela, LocationManager.GPS_PROVIDER) -> {
                localizacaoPrecisa(true, 100, 0.1F, LocationManager.GPS_PROVIDER)
                localizacaoPrecisa(false, 60000, 100F, LocationManager.GPS_PROVIDER)
            }
            Permissao.veficarPermissao(Tela, Permissao.Permissoes.LOCALIZACAO_COARSE) == Permissao.TipoDePermissao.PERMITIDO && LocalizacaoPessoa.providerAtivo(Tela, LocationManager.NETWORK_PROVIDER) -> {
                localizacaoPrecisa(true, 100, 0.1F, LocationManager.NETWORK_PROVIDER)
                localizacaoPrecisa(false, 60000, 100F, LocationManager.NETWORK_PROVIDER)
            }
            else -> {

            }
        }
    }

    private fun localizacaoPrecisa(Modo:Boolean, Tempo:Long, Distancia:Float ,Tipo:String){
        val LocalRecuperados = LocalizacaoPessoa(Tela, Tipo, Tempo, Distancia, this)
        if(Modo){
            LocalRecuperados.removerChamada()
        }
    }

    override fun onLocationChanged(p0: Location?) {
        LocalizacaoLojas.buscarLojas(5.0, GeoLocation(-23.506599, -47.488397), object : LocalizacaoLojas.LocalizacaoLoja {
            override fun lojaRecebida(Id: String, Localizacao:GeoLocation) {
                ListaProximos.adicionar(Id, Localizacao)
            }
        })
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {

    }

    override fun onProviderEnabled(p0: String?) {

    }

    override fun onProviderDisabled(p0: String?) {

    }

}