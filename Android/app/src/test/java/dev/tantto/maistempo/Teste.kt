package dev.tantto.maistempo

import dev.tantto.maistempo.modelos.Local
import org.junit.Test

class Teste {

    @Test
    fun requisicao(){

        val item = Local(10.0, 20.0, "Teste")
        val resultado = item.Nome.isNotBlank()
        assert(resultado)
    }

}