package dev.tantto.maistempo.classes

import android.graphics.Bitmap
import dev.tantto.maistempo.modelos.Lojas
import dev.tantto.maistempo.modelos.Perfil
import dev.tantto.maistempo.google.*

class BuscarLojasImagem() : DatabasePessoaInterface, DatabaseLocaisInterface, DownloadFotoCloud{

    private var Tamanho = 0
    private val ListaLojas = mutableListOf<Lojas>()
    private val ListaLojasImagem = hashMapOf<String, Bitmap>()
    private var Pessoa:Perfil? = null
    private var InterfaceBusca:BuscarConcluida? = null

    constructor(Email: String, Interface: BuscarConcluida) : this() {
        InterfaceBusca = Interface
        DatabaseFirebaseRecuperar.recuperaDadosPessoa(Email, this)
    }

    constructor(Cidade:String, Pessoa: Perfil?, Interface: BuscarConcluida) : this(){
        this.Pessoa = Pessoa
        if(Pessoa != null){
            DatabaseFirebaseRecuperar.recuperaDadosPessoa(Pessoa.email, this)
        } else {
            DatabaseFirebaseRecuperar.recuperarLojasLocais(Cidade, this)
        }
    }

    override fun pessoaRecebida(Pessoa: Perfil) {
        this.Pessoa = Pessoa
        DatabaseFirebaseRecuperar.recuperarLojasLocais(Pessoa.cidade, this)
    }

    override fun dadosRecebidosLojas(Lista: MutableList<Lojas>, Erros: String) {
        baixarImagens(Erros, Lista)
    }

    private fun baixarImagens(Erros: String, Lista: MutableList<Lojas>) {
        if (Erros.isEmpty()) {
            if (Lista.isNotEmpty()) {
                Tamanho = Lista.size
                ListaLojas.addAll(Lista)
                for (Item in Lista) {
                    CloudStorageFirebase().donwloadCloud("${Item.id}.jpg", TipoDonwload.ICONE, this)
                }
            } else {
                InterfaceBusca?.concluido(false, null, null, Pessoa)
            }
        } else {
            InterfaceBusca?.concluido(false, null, null, Pessoa)
        }
    }

    override fun imagemBaixada(Imagem: HashMap<String, Bitmap>?) {
        if(Tamanho == 0){
            InterfaceBusca?.concluido(true, ListaLojas, ListaLojasImagem, Pessoa)
        } else if (Imagem != null) {
            ListaLojasImagem.putAll(Imagem)
            Tamanho -= 1
            if(Tamanho == 0){
                InterfaceBusca?.concluido(true, ListaLojas, ListaLojasImagem, Pessoa)
            }
        }
    }

    interface BuscarConcluida {

        fun concluido(Modo:Boolean, Lista: MutableList<Lojas>?, ListaImagem: HashMap<String, Bitmap>?, Pessoa: Perfil?)

    }

    /*interface BuscarLocalizacao{

        fun concluido(Modo: Boolean, Lista: MutableList<Lojas>)

    }*/
}