package dev.tantto.maistempo.Fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import dev.tantto.maistempo.chaves.Chave
import dev.tantto.maistempo.Classes.Alertas
import dev.tantto.maistempo.google.*
import dev.tantto.maistempo.Modelos.Lojas
import dev.tantto.maistempo.R
import dev.tantto.maistempo.telas.TelaResumoLoja

class FragmentAvaliacao : Fragment() {

    private var Progresso:ProgressBar? = null
    private var NumeroAvailicao:TextView? = null
    private var RatingVoto:RatingBar? = null
    private var Enviar:Button? = null
    private var Loja:Lojas? = null

    private lateinit var referencia:TelaResumoLoja

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_avaliacao_local, container, false)
    }

    override fun onResume() {
        super.onResume()
        configurandoView()
        setandoValores()
    }

    fun setandoReferencias(Item:Lojas, ref:TelaResumoLoja){
        Loja = Item
        referencia = ref
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(Chave.CHAVE_LOJA.valor, Loja)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if(savedInstanceState != null && savedInstanceState.containsKey(Chave.CHAVE_LOJA.valor)){
            Loja = savedInstanceState.getSerializable(Chave.CHAVE_LOJA.valor) as Lojas
        }
    }

    private fun configurandoView() {
        Progresso = this.view?.findViewById<ProgressBar>(R.id.ProgressoVotosAvaliacao)
        NumeroAvailicao = this.view?.findViewById<TextView>(R.id.NumeroAvaliacoesTexto)
        RatingVoto = this.view?.findViewById<RatingBar>(R.id.RatingLocal)
        Enviar = this.view?.findViewById<Button>(R.id.EnviarRating)

        Enviar?.setOnClickListener {
            val nota = RatingVoto?.rating!!
            //Progresso?.progress = ranking.toInt()
            Alertas.criarAlerter(referencia, getString(R.string.RatingAlerta) + RatingVoto?.rating.toString(), R.string.Enviando, 5000).show()
            RatingVoto?.isEnabled = false

            //CloudFunctions.salvarRanking(Loja?.id!!, FirebaseAutenticacao.Autenticacao.currentUser?.email!!, ranking.toDouble(), this)
            DatabaseFirebaseSalvar.enviarRanking(Loja?.id!!, FirebaseAutenticacao.Autenticacao.currentUser?.email!!, nota.toDouble())
        }

    }

    /** override fun resultado(Valor: Resultado) {
        if(Valor == Resultado.SUCESSO){
            Alertas.criarAlerter(referencia, R.string.EnviadoComSucesso, R.string.Sucesso, 5000)
        } else {
            Alertas.criarAlerter(referencia, R.string.ErroAoEnviarRanking, R.string.Erro, 5000)
        }
    }*/

    private fun setandoValores(){
        Progresso?.progress = Loja?.mediaRanking?.toInt()!! * 20
        NumeroAvailicao?.text = String.format(Loja?.quantidadeAvaliacoesRating.toString()  + " " + getString(R.string.Avalicoes))
    }

}