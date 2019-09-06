package dev.tantto.maistempo

import com.firebase.geofire.GeoLocation
import org.junit.Test

class TesteListaProximo {

    @Test
    fun testeRecuperarElemento(){
        val elementoRecuperado = ListaProximos.recuperar("Teste")

        elementoRecuperado?.longitude
    }

    @Test
    fun testeLimpar(){
        ListaProximos.limpar()
    }

    @Test
    fun testeNaoContem(){

        assert(ListaProximos.contem("Teste")){
            if (!ListaProximos.contem("Teste")){
                "Nao existe"
            } else {
                "Existe"
            }
        }

    }

    @Test
    fun testeAdicionar(){
        ListaProximos.adicionar("Teste", GeoLocation(25.6, 62.6))
    }

    @Test
    fun testeContem(){

        testeAdicionar()

        assert(ListaProximos.contem("Teste")){
            if (!ListaProximos.contem("Teste")){
                "Nao existe"
            } else {
                "Existe"
            }
        }

    }

    @Test
    fun testeTamanho(){
        //ListaProximos.tamanho()
    }

}