package dev.tantto.maistempo.Classes

import android.graphics.Bitmap
import dev.tantto.maistempo.Modelos.Lojas
import dev.tantto.maistempo.Modelos.Perfil
import dev.tantto.maistempo.google.*

class BuscarLojasImagem(Email: String, private var Interface: BuscarConcluida) : DatabasePessoaInterface, DatabaseLocaisInterface, DownloadFotoCloud{

    private var Tamanho = 0
    private val ListaLojas = mutableListOf<Lojas>()
    private val ListaLojasImagem = hashMapOf<String, Bitmap>()

    init {
        DatabaseFirebaseRecuperar.recuperaDadosPessoa(Email, this)
    }

    override fun pessoaRecebida(Pessoa: Perfil) {
        DatabaseFirebaseRecuperar.recuperarLojasLocais(Pessoa.cidade, this)
    }

    override fun dadosRecebidosLojas(Lista: MutableList<Lojas>, Erros: String) {
        if(Erros.isEmpty()){
            if(Lista.isNotEmpty()){
                Tamanho = Lista.size -1
                ListaLojas.addAll(Lista)
                for(Item in Lista){
                    CloudStorageFirebase().donwloadCloud("${Item.id}.jpg", TipoDonwload.ICONE, this)
                }
            } else {
                Interface.concluido(false, null, null)
            }
        } else {
            Interface.concluido(false, null, null)
        }
    }

    override fun imagemBaixada(Imagem: HashMap<String, Bitmap>?) {
        if(Tamanho == 0){
            Interface.concluido(true, ListaLojas, ListaLojasImagem)
        } else if (Imagem != null) {
            ListaLojasImagem.putAll(Imagem)
            Tamanho -= 1
        }
    }
}


interface BuscarConcluida {

    fun concluido(Modo:Boolean, Lista: MutableList<Lojas>?, ListaImagem: HashMap<String, Bitmap>?)

}