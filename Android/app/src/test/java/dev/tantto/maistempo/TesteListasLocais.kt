package dev.tantto.maistempo

import org.junit.Test

class TesteListasLocais {

    @Test
    fun testeAdicionarFavoritos(){
        ListaLocais.adicionarFavorito("Teste")
        ListaLocais.refazerFavoritos(mutableListOf())
    }


    @Test
    fun testeAdicionarLoja(){
        ListaLocais.refazer(mutableListOf())
    }

    @Test
    fun testeRemoverFavoritoLojaErrada(){
        ListaLocais.removerFavoritos("Teste")
    }

    @Test
    fun testeLimpar(){
        //ListaLocais.limpar()
    }

    @Test
    fun testeContemFavorito(){
        ListaLocais.contemFavoritos("Teste")
    }

    @Test
    fun teste(){

    }

}