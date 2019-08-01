package dev.tantto.maistempo.Adaptadores

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.tantto.maistempo.Google.CloudStorageFirebase
import dev.tantto.maistempo.Google.DownloadFotoCloud
import dev.tantto.maistempo.Google.TipoDonwload
import dev.tantto.maistempo.Modelos.Perfil
import dev.tantto.maistempo.R

class AdaptadorRanking(private val Contexto:Context) : RecyclerView.Adapter<AdaptadorRanking.ViewHolder>() {

    var lista:List<Perfil> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(Contexto).inflate(R.layout.celula_ranking, parent, false))
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setandoValor()
        holder.colocarValor(lista[position].titulo, lista[position].pontosTotais.toString(), position + 1)
        holder.colocarImagem(lista[position].email)
    }

    fun adicionandoValor(Novos:List<Perfil>){
        lista = Novos
        notifyDataSetChanged()
    }


    class ViewHolder(private val Item: View) : RecyclerView.ViewHolder(Item), DownloadFotoCloud{

        private var Foto:ImageView? = null
        private var Nome:TextView? = null
        private var Pontos:TextView? = null
        private var Posicao:TextView? = null

        fun setandoValor(){
            Foto = Item.findViewById<ImageView>(R.id.FotoRanking)
            Nome = Item.findViewById<TextView>(R.id.NomeRanking)
            Pontos = Item.findViewById<TextView>(R.id.StatusRanking)
            Posicao = Item.findViewById<TextView>(R.id.PosicaoRanking)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Foto?.clipToOutline = true
            }
        }

        fun colocarValor(NomeR:String, PontosR:String, PosicaoR:Int){
            Nome?.text = NomeR
            Pontos?.text = PontosR
            Posicao?.text = String.format("$PosicaoR#")
        }

        fun colocarImagem(Email:String){
            CloudStorageFirebase().donwloadCloud(Email, TipoDonwload.PERFIl, this)
        }

        override fun imagemBaixada(Imagem: Bitmap?) {
            if(Imagem != null){
                Foto?.setImageBitmap(Imagem)
            }
        }

    }

}