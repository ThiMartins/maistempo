package dev.tantto.maistempo.Adaptadores

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import dev.tantto.maistempo.Chaves.Chaves
import dev.tantto.maistempo.ListaFavoritos
import dev.tantto.maistempo.Modelos.Lojas
import dev.tantto.maistempo.R
import dev.tantto.maistempo.Servicos.baixarImagem
import dev.tantto.maistempo.Telas.TelaResumo

class AdaptadorLocal(private val Contexto:Context, private var Lista:List<Lojas>) : RecyclerView.Adapter<AdaptadorLocal.Holder>() {

    private var Backup:List<Lojas>? = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        Backup = Lista
        return Holder(LayoutInflater.from(Contexto).inflate(R.layout.celula_itens, parent, false), Contexto)
    }

    override fun getItemCount(): Int {
        return Lista.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setandoItens()
        holder.adicionandoValores(Lista[position])
        holder.click(Lista[position])
    }

    class Holder(private val Item:View, private val Contexto: Context) : RecyclerView.ViewHolder(Item) {

        private var Titulo:TextView? = null
        private var Status:TextView? = null
        private var Imagem:ImageView? = null
        private var CardLocal:CardView? = null

        fun setandoItens() {
            Titulo = Item.findViewById<TextView>(R.id.TituloLocal)
            Status = Item.findViewById<TextView>(R.id.StatusRanking)
            Imagem = Item.findViewById<ImageView>(R.id.ImagemLocal)
            CardLocal = Item.findViewById<CardView>(R.id.CardItemLocal)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Imagem?.clipToOutline = true
            }

        }

        fun adicionandoValores(Elementos:Lojas){
            Titulo?.text = Elementos.titulo
            Status?.text = Elementos.status[0]

            val DownloadImagem = baixarImagem()
            DownloadImagem.execute(Elementos.imagem)
            Imagem?.setImageBitmap(DownloadImagem.get())

        }

        fun click(position: Lojas){
            CardLocal?.setOnClickListener {
                val Iniciar = Intent(Contexto, TelaResumo::class.java)
                Iniciar.putExtra(Chaves.CHAVE_TELA_PRINCIPAL.valor, position)
                Contexto.startActivity(Iniciar)
            }
        }
    }

    fun filtro(Filtragem:String){
        Lista =
            if(Filtragem.isNotEmpty()){
                Lista.filter { it.titulo.contains(Filtragem) }
            } else{
                Backup!!
            }
        notifyDataSetChanged()
    }

    fun filtroFavoritos(){
        Lista = Lista.filterIndexed { index, lojas ->
            lojas.id == ListaFavoritos.Lista[index]
        }
    }

}
