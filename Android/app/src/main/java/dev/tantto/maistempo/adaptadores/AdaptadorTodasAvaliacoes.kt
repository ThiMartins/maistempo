package dev.tantto.maistempo.adaptadores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.tantto.maistempo.R

class AdaptadorTodasAvaliacoes(private val Lista:MutableMap<String, Double>) : RecyclerView.Adapter<AdaptadorTodasAvaliacoes.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.celula_notas_avaliacoes, parent, false))
    }

    override fun getItemCount(): Int {
        return Lista.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val Nome = Lista.keys.toList()[position]
        val Valores = Lista.values.toList()[position]
        holder.configurandoElementos(Nome , Valores)
    }

    class ViewHolder(private val Item:View) : RecyclerView.ViewHolder(Item){

        fun configurandoElementos(Nome:String, Valor:Double){
            Item.findViewById<TextView>(R.id.NomePessoa).text = Nome.substring(Nome.indexOf("/") + 1)
            Item.findViewById<RatingBar>(R.id.VotoPessoa).rating = Valor.toFloat()
        }

    }

}