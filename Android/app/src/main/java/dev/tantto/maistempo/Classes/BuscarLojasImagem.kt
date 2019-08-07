package dev.tantto.maistempo.Classes

import android.graphics.Bitmap
import dev.tantto.maistempo.Modelos.Lojas
import dev.tantto.maistempo.Modelos.Perfil
import dev.tantto.maistempo.google.*

class BuscarLojasImagem(Email: String, private var Interface: BuscarConcluida) : DatabasePessoaInterface, DatabaseLocaisInterface, DownloadFotoCloud{

    private var Tamanho = 0
    private val ListaLojas = mutableListOf<Lojas>()
    private val ListaLojasImagem = hashMapOf<String, Bitmap>()
    private var Pessoa:Perfil = Perfil()

    init {
        DatabaseFirebaseRecuperar.recuperaDadosPessoa(Email, this)
    }

    override fun pessoaRecebida(Pessoa: Perfil) {
        this.Pessoa = Pessoa
        DatabaseFirebaseRecuperar.recuperarLojasLocais(Pessoa.cidade, this)
    }

    override fun dadosRecebidosLojas(Lista: MutableList<Lojas>, Erros: String) {
        if(Erros.isEmpty()){
            if(Lista.isNotEmpty()){
                Tamanho = Lista.size
                ListaLojas.addAll(Lista)
                for(Item in Lista){
                    CloudStorageFirebase().donwloadCloud("${Item.id}.jpg", TipoDonwload.ICONE, this)
                }
            } else {
                Interface.concluido(false, null, null, Pessoa)
            }
        } else {
            Interface.concluido(false, null, null, Pessoa)
        }
    }

    override fun imagemBaixada(Imagem: HashMap<String, Bitmap>?) {
        if(Tamanho == 0){
            Interface.concluido(true, ListaLojas, ListaLojasImagem, Pessoa)
        } else if (Imagem != null) {
            ListaLojasImagem.putAll(Imagem)
            Tamanho -= 1
            if(Tamanho == 0){
                Interface.concluido(true, ListaLojas, ListaLojasImagem, Pessoa)
            }
        }
    }
}


interface BuscarConcluida {

    fun concluido(Modo:Boolean, Lista: MutableList<Lojas>?, ListaImagem: HashMap<String, Bitmap>?, Pessoa: Perfil)

}