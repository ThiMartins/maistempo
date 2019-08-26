package dev.tantto.maistempo.adaptadores

import android.graphics.Bitmap
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.tantto.maistempo.google.CloudStorageFirebase
import dev.tantto.maistempo.google.DownloadFotoCloud
import dev.tantto.maistempo.google.TipoDonwload
import dev.tantto.maistempo.modelos.Perfil
import dev.tantto.maistempo.R

class AdaptadorRaking : RecyclerView.Adapter<AdaptadorRaking.ViewHolder>() {

    private var lista:List<Perfil> = mutableListOf()
    private var modo:Boolean = false
    private var Posicao:Long = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.celula_ranking, parent, false))
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setandoValor()
        if(modo){
            holder.colocarValor(lista[position].titulo, lista[position].pontosTotais.toString(), Posicao)
        } else {
            holder.colocarValor(lista[position].titulo, lista[position].pontosTotais.toString(), (position + 1).toLong())
        }
        holder.colocarImagem(lista[position].email)
    }

    fun adicionandoValor(Novos:List<Perfil>, Posicao:Long = 0){
        if(Posicao <= 0){
            lista = Novos
            notifyDataSetChanged()
        } else {
            modo = true
            this.Posicao = Posicao
            lista = Novos
            notifyDataSetChanged()
        }
    }


    class ViewHolder(private val Item: View) : RecyclerView.ViewHolder(Item), DownloadFotoCloud{

        private var Foto:ImageView? = null
        private var Nome:TextView? = null
        private var Pontos:TextView? = null
        private var Posicao:TextView? = null

        fun setandoValor(){
            Foto = Item.findViewById(R.id.FotoRanking)
            Nome = Item.findViewById(R.id.NomeRanking)
            Pontos = Item.findViewById(R.id.StatusRanking)
            Posicao = Item.findViewById(R.id.PosicaoRanking)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Foto?.clipToOutline = true
            }
        }

        fun colocarValor(NomeR:String, PontosR:String, PosicaoR:Long = 0){
            Nome?.text = NomeR
            Pontos?.text = String.format("$PontosR pts")
            Posicao?.text = String.format("$PosicaoR#")
        }

        fun colocarImagem(Email:String){
            CloudStorageFirebase().donwloadCloud(Email, TipoDonwload.PERFIl, this)
        }

        override fun imagemBaixada(Imagem: HashMap<String, Bitmap>?) {
            if(Imagem != null){
                Foto?.setImageBitmap(Imagem.values.toList()[0])
            }
        }

    }

}