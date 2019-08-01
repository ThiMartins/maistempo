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

    private var Modo:Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(Contexto).inflate(R.layout.celula_fila, parent, false), Contexto)
    }

    override fun getItemCount(): Int {
        return when (Modo){
            0 -> Lista.filaNormal.size
            1 -> Lista.filaRapida.size
            2 -> Lista.filaPreferencial.size
            else -> 0
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when(Modo){
            0 -> holder.setandoItens(Lista.filaNormal[(Lista.horarioInicio + position).toString()]?.toInt()!!, (Lista.horarioInicio + position))
            1 -> holder.setandoItens(Lista.filaRapida[(Lista.horarioInicio + position).toString()]?.toInt()!!, (Lista.horarioInicio + position))
            2 -> holder.setandoItens(Lista.filaPreferencial[(Lista.horarioInicio + position).toString()]?.toInt()!!, (Lista.horarioInicio + position))
        }
    }

    fun mudarValores(ModoRecebido:Int){
        Modo = ModoRecebido
        notifyDataSetChanged()
    }

    class ViewHolder(private val Item: View, private val Contexto: Context) : RecyclerView.ViewHolder(Item){

        fun setandoItens(Progresso:Int, Horario:Int){
            when {
                Horario <= 9 -> Item.findViewById<TextView>(R.id.HorarioFila).text = String.format("0$Horario:00")
                else -> Item.findViewById<TextView>(R.id.HorarioFila).text = String.format("$Horario:00")
            }
            val ProgressoBarra = Item.findViewById<ProgressBar>(R.id.ProgressoFila1)
            ProgressoBarra.progress = 0

            val ProgressoConvertido= Progresso * 15
            setandoCor(ProgressoConvertido, ProgressoBarra)
            colocandoAnimacao(ProgressoBarra, ProgressoConvertido)

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