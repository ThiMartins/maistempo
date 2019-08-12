package dev.tantto.maistempo.adaptadores

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import dev.tantto.maistempo.chaves.Chave
import dev.tantto.maistempo.ListaBitmap
import dev.tantto.maistempo.ListaLocais
import dev.tantto.maistempo.ListaProximos
import dev.tantto.maistempo.modelos.Lojas
import dev.tantto.maistempo.R
import dev.tantto.maistempo.classes.LocalizacaoPessoa
import dev.tantto.maistempo.telas.TelaResumoLoja
import java.text.NumberFormat
import java.util.*

class AdaptadorFavorito(private val Contexto: Context) : RecyclerView.Adapter<AdaptadorFavorito.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(Contexto).inflate(R.layout.celula_itens, parent, false), Contexto)
    }

    override fun getItemCount(): Int {
        return ListaLocais.tamanhoFavoritos()
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setandoItens()
        if(ListaLocais.tamanho() != 0){
            holder.adicionandoValores(ListaLocais.recuperarFavorito(position)!!)
            holder.click(ListaLocais.recuperarFavorito(position)!!, position)
        }

    }

    class Holder(Item: View, private val Contexto: Context) : RecyclerView.ViewHolder(Item) {

        private var Titulo:TextView = Item.findViewById(R.id.TituloLocal)
        private var Status:TextView = Item.findViewById(R.id.StatusRanking)
        private var Imagem:ImageView = Item.findViewById(R.id.ImagemLocal)
        private var CardLocal: CardView = Item.findViewById(R.id.CardItemLocal)
        private var ResumoFila:View = Item.findViewById(R.id.ModoFilaLocal)
        private var DistanciaLoja = Item.findViewById<TextView>(R.id.DistanciaLoja)

        fun setandoItens() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Imagem.clipToOutline = true
            }

        }

        fun adicionandoValores(Elementos: Lojas){
            Titulo.text = Elementos.titulo

            if(ListaBitmap.tamanho() > 0){
                Imagem.setImageBitmap(ListaBitmap.recuperar(Elementos.id))
            }

            val Horas = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

            if(Horas >= Elementos.horarioInicio && Horas <= Elementos.horarioFinal){
                Status.text = Contexto.getString(R.string.Aberto)
            } else {
                Status.text = Contexto.getString(R.string.Fechado)
            }

            if(ListaProximos.contem(Chave.CHAVE_MINHA_LOCALIZCAO.valor)){
                val Coordenadas = LatLng(Elementos.latitude, Elementos.longitude)
                val Valores = ListaProximos.recuperar(Chave.CHAVE_MINHA_LOCALIZCAO.valor)
                val MinhaCoordenadas = LatLng(Valores.longitude, Valores.latitude)
                val Resultado = NumberFormat.getInstance()
                Resultado.maximumFractionDigits = 2
                Log.i("Teste", MinhaCoordenadas.toString())
                Log.i("Teste", Coordenadas.toString())
                DistanciaLoja.text = String.format(Resultado.format((LocalizacaoPessoa.calcularDistancia(MinhaCoordenadas, Coordenadas)) / 1000) .toString() + "\n" + Contexto.getString(R.string.KM))
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
        ListaLocais.filtroFavoritos(Filtragem)
        notifyDataSetChanged()
    }

    fun reloadData(){
        notifyDataSetChanged()
    }
}