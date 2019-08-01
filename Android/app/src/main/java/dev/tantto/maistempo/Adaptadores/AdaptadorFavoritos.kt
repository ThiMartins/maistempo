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
import dev.tantto.maistempo.ListaBitmap
import dev.tantto.maistempo.ListaLocais
import dev.tantto.maistempo.Modelos.Lojas
import dev.tantto.maistempo.R
import dev.tantto.maistempo.Telas.TelaResumo
import java.util.*

class AdaptadorFavoritos(private val Contexto: Context) : RecyclerView.Adapter<AdaptadorFavoritos.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(Contexto).inflate(R.layout.celula_itens, parent, false), Contexto)
    }

    override fun getItemCount(): Int {
        return ListaLocais.tamanhoFavoritos()
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setandoItens()
        holder.adicionandoValores(ListaLocais.recuperarFavorito(position)!!, position)
        holder.click(ListaLocais.recuperarFavorito(position)!!, position)

    }

    class Holder(private val Item: View, private val Contexto: Context) : RecyclerView.ViewHolder(Item) {

        private var Titulo: TextView? = null
        private var Status: TextView? = null
        private var Imagem: ImageView? = null
        private var CardLocal: CardView? = null

        fun setandoItens() {
            Titulo = Item.findViewById<TextView>(R.id.TituloLocal)
            Status = Item.findViewById<TextView>(R.id.StatusRanking)
            Imagem = Item.findViewById<ImageView>(R.id.ImagemLocal)
            CardLocal = Item.findViewById<CardView>(R.id.CardItemLocal)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Imagem?.clipToOutline = true
            }

        }

        fun adicionandoValores(Elementos: Lojas, position: Int){
            Titulo?.text = Elementos.titulo

            val IndexBitmap = ListaLocais.recuperarPosicoesFavoritosBitmap()
            Imagem?.setImageBitmap(ListaBitmap.recuperar(IndexBitmap[position]))

            val Horas = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

            if(Horas >= Elementos.horarioInicio && Horas <= Elementos.horariofinal){
                Status?.text = Contexto.getString(R.string.Aberto)
            } else {
                Status?.text = Contexto.getString(R.string.Fechado)
            }
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
        ListaLocais.filtroFavoritos(Filtragem)
        notifyDataSetChanged()
    }

    fun reloadData(){
        notifyDataSetChanged()
    }
}