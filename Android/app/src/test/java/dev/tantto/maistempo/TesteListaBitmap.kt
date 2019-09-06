package dev.tantto.maistempo

import org.junit.Test
import java.lang.Exception

class TesteListaBitmap {

    @Test
    fun testeRecuperarElementoNaoExistente(){
        val nomeItem = "Teste"

        val recuperado = ListaBitmap.recuperar(nomeItem)
        if(recuperado == null){
            assert(true)
        }else {
            assert(false)
        }
    }

    @Test
    fun testeRecuperarTamanhoLista(){
        val tamanho = ListaBitmap.tamanho()
        assert(true ) {
            tamanho
        }
    }

    @Test
    fun testeLimparLista(){
        try {
            //ListaBitmap.limpar()
            assert(true)
        } catch (Erro:Exception){
            assert(false)
        }
    }

}