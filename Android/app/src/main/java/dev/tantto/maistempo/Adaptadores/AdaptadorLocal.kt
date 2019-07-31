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
import dev.tantto.maistempo.ListaLocais
import dev.tantto.maistempo.Modelos.Lojas
import dev.tantto.maistempo.R
import dev.tantto.maistempo.Telas.TelaResumo

class AdaptadorLocal(private val Contexto:Context) : RecyclerView.Adapter<AdaptadorLocal.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(Contexto).inflate(R.layout.celula_itens, parent, false), Contexto)
    }

    override fun getItemCount(): Int {
        return ListaLocais.tamanho()
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setandoItens()
        holder.adicionandoValores(ListaLocais.recuperar(position), position)
        holder.click(ListaLocais.recuperar(position), position)

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

        fun adicionandoValores(Elementos:Lojas, position: Int){
            Titulo?.text = Elementos.titulo
            Status?.text = Elementos.status[0]
            //Imagem?.setImageBitmap(ListaBitmap.recuperar(position))
        }

        fun click(positionLoja: Lojas, position: Int){
            CardLocal?.setOnClickListener {
                val Iniciar = Intent(Contexto, TelaResumo::class.java)
                Iniciar.putExtra(Chaves.CHAVE_TELA_PRINCIPAL.valor, positionLoja)
                Iniciar.putExtra(Chaves.CHAVE_POSICAO_LISTA.valor, position)
                Contexto.startActivity(Iniciar)
            }
        }
    }

    fun filtro(Filtragem:String){
        ListaLocais.filto(Filtragem)
        notifyDataSetChanged()
    }
}
