package dev.tantto.maistempo.Adaptadores

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import dev.tantto.maistempo.Modelos.Lojas
import dev.tantto.maistempo.R
import dev.tantto.maistempo.telas.TelaResumo

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
        holder.SetandoItens()
        holder.AdicionandoValores(Lista[position])
        holder.Click(Lista.get(position))
    }

    class Holder(private val Item:View, private val Contexto: Context) : RecyclerView.ViewHolder(Item) {

        private var Titulo:TextView? = null
        private var Status:TextView? = null
        private var Imagem:ImageView? = null
        private var Card:CardView? = null

        fun SetandoItens() {
            Titulo = Item.findViewById<TextView>(R.id.TituloLocal)
            Status = Item.findViewById<TextView>(R.id.StatusLocal)
            Imagem = Item.findViewById<ImageView>(R.id.ImagemLocal)
            Card = Item.findViewById<CardView>(R.id.CardItem)
        }

        fun AdicionandoValores(Elementos:Lojas){
            Titulo?.text = Elementos.titulo
            Status?.text = Elementos.status[0]
            Imagem?.setImageResource(R.drawable.maistempocircle)
        }

        fun Click(position: Lojas){
            Card?.setOnClickListener {
                val Iniciar = Intent(Contexto, TelaResumo::class.java)
                Iniciar.putExtra("TELAPRINCIPAL", position)
                Contexto.startActivity(Iniciar)
            }
        }

    }

    fun Filtro(Filtragem:String){
        if(Filtragem.isNotEmpty()){
            Lista = Lista.filter {
                it.titulo.contains(Filtragem)
            }

        } else{
            Lista = Backup!!
        }
        notifyDataSetChanged()
    }

}
