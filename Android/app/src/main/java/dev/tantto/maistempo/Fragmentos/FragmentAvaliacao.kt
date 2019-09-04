package dev.tantto.maistempo.fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import dev.tantto.maistempo.chaves.Chave
import dev.tantto.maistempo.classes.Alertas
import dev.tantto.maistempo.google.*
import dev.tantto.maistempo.modelos.Lojas
import dev.tantto.maistempo.R
import dev.tantto.maistempo.adaptadores.AdaptadorTodasAvaliacoes
import dev.tantto.maistempo.modelos.Perfil
import dev.tantto.maistempo.telas.TelaResumoLoja

class FragmentAvaliacao : Fragment() {

    private var Progresso:ProgressBar? = null
    private var NumeroAvailicao:TextView? = null
    private var RatingVoto:RatingBar? = null
    private var Enviar:Button? = null
    private var Loja:Lojas? = null
    private var VerAvaliacoes:Button? = null

    private var referencia:TelaResumoLoja? = null

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
        setandoValores()
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
        Progresso = this.view?.findViewById(R.id.ProgressoVotosAvaliacao)
        NumeroAvailicao = this.view?.findViewById(R.id.NumeroAvaliacoesTexto)
        RatingVoto = this.view?.findViewById(R.id.RatingLocal)
        Enviar = this.view?.findViewById(R.id.EnviarRating)
        VerAvaliacoes = this.view?.findViewById(R.id.VerTodasAvaliacoes)

        VerAvaliacoes?.setOnClickListener {
            val alertaCarregando = Alertas.alertaCarregando(this.requireContext())
            alertaCarregando.show()
            CloudFunctions.recuperarRaking(Loja?.id!!, object  : CloudFunctions.ListaRanking{
                override fun resultado(lista: HashMap<String, Double>?) {
                    alertaCarregando.dismiss()
                    if(lista != null){
                        val ListaBottom = BottomSheetDialog(this@FragmentAvaliacao.requireContext())
                        ListaBottom.setContentView(R.layout.todas_avaliacao)
                        ListaBottom.show()
                        val ListaRecycler = ListaBottom.findViewById<RecyclerView>(R.id.TodasAvaliacoes)
                        ListaRecycler?.adapter = AdaptadorTodasAvaliacoes(lista.toMutableMap())
                    }
                }
            })
        }

        Enviar?.setOnClickListener {
            val nota = RatingVoto?.rating!!
            if(referencia != null){
                Alertas.criarAlerter(referencia!!, getString(R.string.RatingAlerta) + RatingVoto?.rating.toString(), R.string.Enviando, 5000).show()
            }
            RatingVoto?.isEnabled = false

            val email = FirebaseAutenticacao.Autenticacao.currentUser?.email!!
            DatabaseFirebaseRecuperar.recuperaDadosPessoa(email, object : DatabasePessoaInterface{
                override fun pessoaRecebida(Pessoa: Perfil) {
                    val nome = Pessoa.titulo
                    val idNota = String.format("$email/$nome")

                    DatabaseFirebaseSalvar.adicionarPontos(email, 1, TipoPontos.PONTOS_AVALIACAO)
                    CloudFunctions.adicionarNotaLoja(idNota, nota.toDouble(), Loja?.id!!)
                    if(referencia != null){
                        Alertas.criarAlerter(referencia!!, R.string.Atualizando, R.string.Aguardando).show()
                    }
                    referencia?.recuperarLoja(Loja?.id!!)
                }
            })
        }

    }

    fun atualizarLoja(Loja:Lojas){
        this.Loja = Loja
        setandoValores()
    }

    private fun setandoValores(){
        Progresso?.progress = Loja?.mediaRanking?.toInt()!! * 20
        NumeroAvailicao?.text = String.format(Loja?.quantidadeAvaliacoesRating.toString()  + " " + getString(R.string.Avalicoes))
    }

}