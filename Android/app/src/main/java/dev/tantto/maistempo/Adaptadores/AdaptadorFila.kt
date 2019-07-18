package dev.tantto.maistempo.Adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import dev.tantto.maistempo.R

class AdaptadorFila(private var Contexto:Context, private var Lista:List<String>) : RecyclerView.Adapter<AdaptadorFila.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(Contexto).inflate(R.layout.celula_fila, parent, false))
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.SetandoItens()
    }

    class ViewHolder(private val Item: View) : RecyclerView.ViewHolder(Item){

        fun SetandoItens(){
            Item.findViewById<ProgressBar>(R.id.ProgressoFila).progress = 50
        }

    }
}