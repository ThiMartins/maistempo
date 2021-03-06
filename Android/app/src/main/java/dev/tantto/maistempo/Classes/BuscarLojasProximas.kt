package dev.tantto.maistempo.classes

import android.app.Activity
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import com.firebase.geofire.GeoLocation
import dev.tantto.maistempo.ListaProximos
import dev.tantto.maistempo.chaves.Chave

class BuscarLojasProximas(private val Tela:Activity, private val Raio:Double) : LocationListener {

    private var InterfaceGeral:BuscaConcluida? = null
    private var Modo:Boolean = false
    private var LocalRecuperados:LocalizacaoPessoa? = null

    fun procurarProximos(Interface:BuscaConcluida){

        InterfaceGeral = Interface

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
                Interface.resultado(false)
            }
        }
    }

    private fun localizacaoPrecisa(Modo:Boolean, Tempo:Long, Distancia:Float ,Tipo:String){
        LocalRecuperados = LocalizacaoPessoa(Tela, Tipo, Tempo, Distancia, this)
        this.Modo = Modo
    }

    override fun onLocationChanged(p0: Location?) {
        if(p0 != null){
            LocalizacaoLojas.buscarLojas(Raio, GeoLocation(p0.latitude, p0.longitude), object : LocalizacaoLojas.LocalizacaoLoja {
                override fun lojaRecebida(Id: String?, Localizacao:GeoLocation?) {
                    if(Id != null && Localizacao != null){
                        ListaProximos.adicionar(Id, Localizacao)
                        if(!ListaProximos.contem(Chave.CHAVE_MINHA_LOCALIZCAO.valor)){
                            ListaProximos.adicionar(Chave.CHAVE_MINHA_LOCALIZCAO.valor, GeoLocation(p0.latitude, p0.longitude))
                        }
                    } else {
                        InterfaceGeral?.resultado(false)
                    }
                    InterfaceGeral?.resultado(true)
                    if(Modo){
                        LocalRecuperados?.removerChamada()
                    }
                }
            })
        } else {
            InterfaceGeral?.resultado(false)
        }
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {

    }

    override fun onProviderEnabled(p0: String?) {

    }

    override fun onProviderDisabled(p0: String?) {

    }

    interface BuscaConcluida{

        fun resultado(Modo: Boolean)

    }

}