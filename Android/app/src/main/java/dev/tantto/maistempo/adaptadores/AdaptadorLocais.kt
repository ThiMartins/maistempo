package dev.tantto.maistempo.adaptadores

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
import dev.tantto.maistempo.chaves.Chave
import dev.tantto.maistempo.ListaBitmap
import dev.tantto.maistempo.ListaLocais
import dev.tantto.maistempo.modelos.Lojas
import dev.tantto.maistempo.R
import dev.tantto.maistempo.telas.TelaResumoLoja
import java.util.*

class AdaptadorLocais(private val Contexto:Context) : RecyclerView.Adapter<AdaptadorLocais.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.celula_itens, parent, false), Contexto)
    }

    override fun getItemCount(): Int {
        return ListaLocais.tamanho()
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.adicionandoValores(ListaLocais.recuperar(position), position)
        holder.click(ListaLocais.recuperar(position), position)

    }

    class Holder(Item:View, private val Contexto: Context) : RecyclerView.ViewHolder(Item) {

        private var Titulo = Item.findViewById<TextView>(R.id.TituloLocal)
        private var Status = Item.findViewById<TextView>(R.id.StatusRanking)
        private var Imagem = Item.findViewById<ImageView>(R.id.ImagemLocal)
        private var CardLocal= Item.findViewById<CardView>(R.id.CardItemLocal)
        private var ResumoFila= Item.findViewById<View>(R.id.ModoFilaLocal)

        init {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Imagem.clipToOutline = true
            }
            ResumoFila
        }

        fun adicionandoValores(Elementos:Lojas, position: Int){
            Titulo.text = Elementos.titulo
            if(ListaBitmap.tamanho() >= position + 1){
                Imagem.setImageBitmap(ListaBitmap.recuperar(Elementos.id))
            }

            val Horas = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

            if(Horas >= Elementos.horarioInicio && Horas <= Elementos.horarioFinal){
                Status.text = Contexto.getString(R.string.Aberto)
            } else {
                Status.text = Contexto.getString(R.string.Fechado)
            }

            setandoBackground(fazerMedia(Elementos, Horas))
        }

        private fun setandoBackground(MediaFinal: Double) {
            if (MediaFinal <= 33.0) {
                ResumoFila.setBackgroundResource(R.drawable.modo_fila_vazio)
            } else if (MediaFinal > 33.0 && MediaFinal <= 66.0) {
                ResumoFila.setBackgroundResource(R.drawable.modo_fila_medio)
            } else {
                ResumoFila.setBackgroundResource(R.drawable.modo_fila_lotado)
            }
        }

        private fun fazerMedia(Elementos: Lojas, Horas: Int): Double {
            val Valores = mutableListOf<Double>()

            if (Elementos.filaNormal.containsKey(Horas.toString())) {
                Valores.add(Elementos.filaNormal[Horas.toString()]!!)
            }
            if (Elementos.filaRapida.containsKey(Horas.toString())) {
                Valores.add(Elementos.filaRapida[Horas.toString()]!!)
            }
            if (Elementos.filaPreferencial.containsKey(Horas.toString())) {
                Valores.add(Elementos.filaPreferencial[Horas.toString()]!!)
            }

            var MediaFinal = 1.0
            for (Valor in Valores) {
                MediaFinal += Valor
            }

            if (Valores.size != 0) {
                MediaFinal /= Valores.size
            }
            return MediaFinal
        }

        fun click(positionLoja: Lojas, position: Int){
            CardLocal.setOnClickListener {
                val Iniciar = Intent(Contexto, TelaResumoLoja::class.java)
                Iniciar.putExtra(Chave.CHAVE_TELA_PRINCIPAL.valor, positionLoja)
                Iniciar.putExtra(Chave.CHAVE_POSICAO_LISTA.valor, position)
                Contexto.startActivity(Iniciar)
            }
        }
    }

    fun filtro(Filtragem:String){
        ListaLocais.filto(Filtragem)
        notifyDataSetChanged()
    }
}
