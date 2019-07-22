package dev.tantto.maistempo.Adaptadores

import android.content.Context
import android.graphics.PorterDuff
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.tantto.maistempo.Modelos.Lojas
import dev.tantto.maistempo.R

class AdaptadorFila(private var Contexto:Context, private var Lista:Lojas) : RecyclerView.Adapter<AdaptadorFila.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(Contexto).inflate(R.layout.celula_fila, parent, false), Contexto)
    }

    override fun getItemCount(): Int {
        return Lista.fila.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.SetandoItens(Lista.fila.get(position), Lista.horarios.get(position))
    }

    class ViewHolder(private val Item: View, private val Contexto: Context) : RecyclerView.ViewHolder(Item){

        fun SetandoItens(Progresso:Int, Horario:String){
            val ProgressoBarra = Item.findViewById<ProgressBar>(R.id.ProgressoFila)
            ProgressoBarra.progress = Progresso
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                when {
                    Progresso <= 10 -> ProgressoBarra.progressDrawable.setColorFilter(Contexto.getColor(R.color.VerdeClaro), PorterDuff.Mode.SRC_IN)
                    Progresso <= 20 -> ProgressoBarra.progressDrawable.setColorFilter(Contexto.getColor(R.color.Verde), PorterDuff.Mode.SRC_IN)
                    Progresso <= 30 -> ProgressoBarra.progressDrawable.setColorFilter(Contexto.getColor(R.color.VerdeEscuro), PorterDuff.Mode.SRC_IN)
                    Progresso <= 40 -> ProgressoBarra.progressDrawable.setColorFilter(Contexto.getColor(R.color.AmareloClaro), PorterDuff.Mode.SRC_IN)
                    Progresso <= 50 -> ProgressoBarra.progressDrawable.setColorFilter(Contexto.getColor(R.color.Amarelo), PorterDuff.Mode.SRC_IN)
                    Progresso <= 60 -> ProgressoBarra.progressDrawable.setColorFilter(Contexto.getColor(R.color.AmreloEscuro), PorterDuff.Mode.SRC_IN)
                    Progresso <= 70 -> ProgressoBarra.progressDrawable.setColorFilter(Contexto.getColor(R.color.VermelhoClaro), PorterDuff.Mode.SRC_IN)
                    Progresso <= 80 -> ProgressoBarra.progressDrawable.setColorFilter(Contexto.getColor(R.color.Vermelho), PorterDuff.Mode.SRC_IN)
                    Progresso <= 90 -> ProgressoBarra.progressDrawable.setColorFilter(Contexto.getColor(R.color.VerdeEscuro), PorterDuff.Mode.SRC_IN)
                    else -> ProgressoBarra.progressDrawable.setColorFilter(Contexto.getColor(R.color.VermelhoEscuro), PorterDuff.Mode.SRC_IN)
                }
            }
            Item.findViewById<TextView>(R.id.HorarioFila).text = Horario
        }

    }
}