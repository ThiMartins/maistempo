package dev.tantto.maistempo.Adaptadores

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.PorterDuff
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
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
        holder.setandoItens(Lista.fila[position], Lista.horarios[position])
    }

    class ViewHolder(private val Item: View, private val Contexto: Context) : RecyclerView.ViewHolder(Item){

        fun setandoItens(Progresso:Int, Horario:String){
            val ProgressoBarra = Item.findViewById<ProgressBar>(R.id.ProgressoFila1)
            setandoCor(Progresso, ProgressoBarra)
            Item.findViewById<TextView>(R.id.HorarioFila).text = Horario
            colocandoAnimacao(ProgressoBarra, Progresso)
            setandoCor(50, Item.findViewById(R.id.ProgressoFila2))
            setandoCor(27, Item.findViewById(R.id.ProgressoFila3))
            colocandoAnimacao(Item.findViewById(R.id.ProgressoFila2), 50)
            colocandoAnimacao(Item.findViewById(R.id.ProgressoFila3), 27)
        }

        private fun colocandoAnimacao(ProgressoBarra: ProgressBar?, Progresso: Int) {
            val Animacao = ObjectAnimator.ofInt(ProgressoBarra, "progress", Progresso)
            when {
                Progresso >= 50 -> Animacao.duration = 1000
                Progresso >= 25 -> Animacao.duration = 500
                Progresso < 25 -> Animacao.duration = 250
            }
            Animacao.interpolator = LinearInterpolator()
            Animacao.start()

        }

        private fun setandoCor(Progresso: Int, ProgressoBarra: ProgressBar) {
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
        }

    }
}