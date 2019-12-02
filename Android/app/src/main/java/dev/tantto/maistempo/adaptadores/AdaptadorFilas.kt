package dev.tantto.maistempo.adaptadores

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
import dev.tantto.maistempo.modelos.Lojas
import dev.tantto.maistempo.R
import dev.tantto.maistempo.google.FirebaseAutenticacao

class AdaptadorFilas(private var Contexto:Context, private var Lista:Lojas) : RecyclerView.Adapter<AdaptadorFilas.ViewHolder>() {

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
        val ChaveNormal = Lista.filaNormal.keys.toList().sortedBy { it.toInt() }
        val ChaveRapida = Lista.filaRapida.keys.toList().sortedBy { it.toInt() }
        val ChavePreferencial = Lista.filaPreferencial.keys.sortedBy { it.toInt() }
        when(Modo){
            0 -> {
                val ValorProgresso = Lista.filaNormal[ChaveNormal[position]].toString().toDouble() * 16.6
                holder.setandoItens(ValorProgresso.toInt(), ChaveNormal[position].toInt())
            }
            1 -> {
                val ValorProgresso = Lista.filaRapida[ChaveRapida[position]].toString().toDouble() * 16.6
                holder.setandoItens(ValorProgresso.toInt(), ChaveRapida[position].toInt())
            }
            2 -> {
                val ValorProgresso = Lista.filaPreferencial[ChavePreferencial[position]].toString().toDouble() * 16.6
                holder.setandoItens(ValorProgresso.toInt(), ChavePreferencial[position].toInt())
            }
        }
    }

    fun mudarValores(ModoRecebido:Int){
        Modo = ModoRecebido
        notifyDataSetChanged()
    }

    fun atualizarLoja(Loja:Lojas){
        Lista = Loja
        notifyDataSetChanged()
    }

    class ViewHolder(private val Item: View, private val Contexto: Context) : RecyclerView.ViewHolder(Item){

        fun setandoItens(Progresso:Int, Horario:Int){
            when {
                Horario <= 9 -> Item.findViewById<TextView>(R.id.HorarioFila).text = String.format("0$Horario:00")
                else -> Item.findViewById<TextView>(R.id.HorarioFila).text = String.format("$Horario:00")
            }
            val ProgressoBarra = Item.findViewById<ProgressBar>(R.id.ProgressoFila1)
            ProgressoBarra.progress = 100

            setandoCor(Progresso + 1, ProgressoBarra)
            colocandoAnimacao(ProgressoBarra, Progresso + 1)

        }

        private fun colocandoAnimacao(ProgressoBarra: ProgressBar?, Progresso: Int) {
            val Animacao = ObjectAnimator.ofInt(ProgressoBarra, "progress", Progresso)
            when {
                Progresso >= 50 -> Animacao.duration = 250
                Progresso >= 25 -> Animacao.duration = 500
                Progresso < 25 -> Animacao.duration = 1000
            }
            Animacao.interpolator = LinearInterpolator()
            Animacao.start()

        }

        @Suppress("DEPRECATION")
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