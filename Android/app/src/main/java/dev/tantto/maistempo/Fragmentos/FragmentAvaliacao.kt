package dev.tantto.maistempo.Fragmentos

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
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
        return inflater.inflate(R.layout.fragment_avaliacao_local, container, false)
    }

    override fun onResume() {
        super.onResume()
        configurandoView()
        setandoValores()
    }

    fun setandoReferencias(Item:Lojas, Ref:TelaResumo){
        Loja = Item
        Referencia = Ref
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(CHAVE_LOJA, Loja)
    }

    private fun configurandoView() {
        Progresso = this.view?.findViewById<ProgressBar>(R.id.ProgressoVotosAvaliacao)
        NumeroAvailicao = this.view?.findViewById<TextView>(R.id.NumeroAvaliacoesTexto)
        RatingVoto = this.view?.findViewById<RatingBar>(R.id.RatingLocal)
        Enviar = this.view?.findViewById<Button>(R.id.EnviarRating)

        Enviar?.setOnClickListener {
            val nota = RatingVoto?.rating!! * 20
            //enviar nota
            Alertas.criarAlerter(Referencia, R.string.RatingAlerta, R.string.Enviando, 5000).show()
            RatingVoto?.isEnabled = false
        }
    }

    private fun setandoValores(){
        Progresso?.progress = Loja?.mediaRating?.toInt()!! * 20
        NumeroAvailicao?.text = String.format(Loja?.avaliacoesRating.toString()  + " " + getString(R.string.Avalicoes))
    }

}