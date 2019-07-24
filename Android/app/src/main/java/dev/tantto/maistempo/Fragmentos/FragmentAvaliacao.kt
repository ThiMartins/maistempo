package dev.tantto.maistempo.Fragmentos

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import dev.tantto.maistempo.Classes.Alertas
import dev.tantto.maistempo.Modelos.Lojas
import dev.tantto.maistempo.R
import dev.tantto.maistempo.Telas.TelaResumo

class FragmentAvaliacao : Fragment() {

    private val CHAVE_LOJA = "LOJA"

    private var Progresso:ProgressBar? = null
    private var NumeroAvailicao:TextView? = null
    private var RatingVoto:RatingBar? = null
    private var Enviar:Button? = null
    private var Loja:Lojas? = null

    private lateinit var Referencia:TelaResumo

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val V = inflater.inflate(R.layout.fragment_avaliacao_local, container, false)
        configurandoView(V)
        setandoValores()
        return V
    }

    fun setandoReferencias(Item:Lojas, Ref:TelaResumo){
        Loja = Item
        Referencia = Ref
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(CHAVE_LOJA, Loja)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        //if(!savedInstanceState?.isEmpty!!){
        //    Loja = savedInstanceState.getSerializable(CHAVE_LOJA) as Lojas
        //}
    }

    private fun configurandoView(V: View) {
        Progresso = V.findViewById<ProgressBar>(R.id.ProgressoVotosAvaliacao)
        NumeroAvailicao = V.findViewById<TextView>(R.id.NumeroAvaliacoesTexto)
        RatingVoto = V.findViewById<RatingBar>(R.id.RatingLocal)
        Enviar = V.findViewById<Button>(R.id.EnviarRating)

        Enviar?.setOnClickListener {
            val nota = RatingVoto?.rating!! * 20
            //enviar nota
            Alertas.CriarTela(Referencia, R.string.RatingAlerta, R.string.Enviando, 5000).show()
            RatingVoto?.isEnabled = false
        }
    }

    private fun setandoValores(){
        Progresso?.progress = Loja?.mediaRating?.toInt()!! * 20
        NumeroAvailicao?.text = String.format(Loja?.avaliacoesRating.toString())
    }

}