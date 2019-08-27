package dev.tantto.maistempo.fragmentos

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import dev.tantto.barra_progresso.BarraProgresso
import dev.tantto.barra_progresso.OnBarraChanged
import dev.tantto.maistempo.adaptadores.AdaptadorFilas
import dev.tantto.maistempo.chaves.Chave
import dev.tantto.maistempo.classes.Alertas
import dev.tantto.maistempo.google.*
import dev.tantto.maistempo.modelos.Lojas
import dev.tantto.maistempo.R
import dev.tantto.maistempo.classes.Dados
import dev.tantto.maistempo.telas.TelaResumoLoja
import java.util.*


class FragmentResumo : Fragment(), FunctionsInterface {

    private var referencia:TelaResumoLoja? = null
    private val handler = Handler(Looper.getMainLooper())

    private var Lista:RecyclerView? = null
    private var NumeroAvaliacoes:TextView? = null
    private var ProgressoFila:BarraProgresso? = null
    private var Tabs:TabLayout? = null
    private var LojaInfo:Lojas? = null
    private var NovaLoja:Lojas? = null
    private var adaptador:AdaptadorFilas? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_fila, container, false)
    }

    override fun onResume() {
        super.onResume()
        configurandoView()
        configurandoAdapter()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(Chave.CHAVE_LOJA.valor, LojaInfo)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if(savedInstanceState != null && savedInstanceState.containsKey(Chave.CHAVE_LOJA.valor)){
            LojaInfo = savedInstanceState.getSerializable(Chave.CHAVE_LOJA.valor) as Lojas
        }
    }

    fun atualizar(){
        Tabs?.getTabAt(0)?.select()
    }

    fun passandoLja(Ref:Lojas, refTelaLoja:TelaResumoLoja){
        LojaInfo = Ref
        referencia = refTelaLoja
    }

    fun atualizarLoja(Ref: Lojas){
        LojaInfo = Ref
        adaptador?.atualizarLoja(Ref)
    }

    private fun configurandoView() {
        recuperandoView()
        configurandoTab()

        ProgressoFila?.addOnBarraChanged(object : OnBarraChanged{
            override fun setOnClickListener(v: BarraProgresso?) {

            }

            override fun setOnBarraChangeListener(v: BarraProgresso?, value: Float) {

            }

            override fun setOnFinishListener(v: BarraProgresso?) {
                if(ProgressoFila?.isEnabled!!){
                    val ConfirmarcaoBuilder = AlertDialog.Builder(context)
                    ConfirmarcaoBuilder.setTitle(R.string.Atencao)
                    ConfirmarcaoBuilder.setMessage(R.string.DesejaEnvia)
                    ConfirmarcaoBuilder.setPositiveButton(R.string.Sim){ _, _ ->
                        enviarNota()
                    }
                    ConfirmarcaoBuilder.setNegativeButton(R.string.Nao, null)
                    ConfirmarcaoBuilder.create().show()
                } else {
                    Snackbar.make(this@FragmentResumo.view?.findViewById<NestedScrollView>(R.id.FragmentoFilaId)!!, R.string.ErroEnviarNovamente, Snackbar.LENGTH_LONG).show()
                }
            }
        })

        val contexto = context
        if(contexto != null){
            val Horas = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            val Dia = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            val Momento = Dados.verificarLojaHorario(contexto, LojaInfo?.id!!)
            if(Momento == "$Horas:$Dia"){
                ProgressoFila?.isEnabled = false
            }
        }

        NumeroAvaliacoes?.text = String.format(LojaInfo?.quantidadeAvaliacoesFila.toString() + " "+ getString(R.string.Avalicoes))
    }

    private fun enviarNota() {
        val email = FirebaseAutenticacao.Autenticacao.currentUser?.email
        if (!email.isNullOrEmpty()) {
            mostrarAlerta()
            DatabaseFirebaseSalvar.adicionarPontos(email, 1, TipoPontos.PONTOS_FILA)

            val Horas = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            val Tipo = when {
                Tabs?.selectedTabPosition == 0 -> TipoFila.FilaNormal
                Tabs?.selectedTabPosition == 1 -> TipoFila.FilaRapida
                Tabs?.selectedTabPosition == 2 -> TipoFila.FilaPrererencial
                else -> null
            }


            val valor = ProgressoFila?.progresso!!
            val valorNota = when {
                valor < 14.28 -> 0
                valor >= 14.28 && valor < 28.56 -> 1
                valor >= 28.56 && valor < 42.84 -> 2
                valor >= 42.84 && valor < 57.12 -> 3
                valor >= 57.12 && valor < 71.4 -> 4
                valor >= 71.4 && valor < 85.68 -> 5
                else -> 6
            }

            CloudFunctions.adicionarNotaFila(
                LojaInfo?.id!!,
                Tipo?.valor!!,
                valorNota.toDouble(),
                Horas.toString(),
                this@FragmentResumo
            )
            if (referencia != null) {
                Alertas.criarAlerter(referencia!!, R.string.EnviandoNota, R.string.Enviando, 10000)
                    .show()
            }
        }
    }

    private fun recuperandoView() {
        Lista = this.view?.findViewById(R.id.ListaFila)
        NumeroAvaliacoes = this.view?.findViewById(R.id.NumeroAvaliacoes)
        Tabs = this.view?.findViewById(R.id.TabLayoutFila)
        ProgressoFila = this.view?.findViewById(R.id.ProgressoFilaVoto)
    }

    private fun configurandoTab() {
        Tabs?.getTabAt(0)?.setText(R.string.Normal)
        Tabs?.getTabAt(1)?.setText(R.string.Rapida)
        Tabs?.getTabAt(2)?.setText(R.string.Preferencial)
        Tabs?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {
                
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                adaptador?.mudarValores(Tabs?.selectedTabPosition!!)
            }
        })

    }

    override fun resultado(Valor: Resultado) {

        if(referencia != null){
            if(Valor == Resultado.ERRO){
                Alertas.criarAlerter(referencia!!, R.string.ErroEnviar, R.string.Atencao, 10000).show()
            } else {
                val contexto = context
                if(contexto != null){
                    val Horas = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                    val Dia = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                    Dados.salvarLoja(contexto, LojaInfo?.id!!, "$Horas:$Dia")
                    ProgressoFila?.isEnabled = false
                }
                val alerta = Alertas.criarAlerter(referencia!!, R.string.Atualizando, R.string.Aguardando)
                alerta.show()

                object : Runnable{
                    override fun run() {
                        if(NovaLoja == null){
                            handler.postDelayed(this, 2000)
                            verificarAtualizacao()
                        } else {
                            NumeroAvaliacoes?.text = String.format(NovaLoja?.quantidadeAvaliacoesFila.toString() + " "+ getString(R.string.Avalicoes))
                            alerta.setDuration(1000)
                            LojaInfo = NovaLoja
                            adaptador?.atualizarLoja(NovaLoja!!)
                            adaptador?.notifyDataSetChanged()
                            NovaLoja = null
                        }
                    }
                }.run()
            }
        }
    }

    private fun verificarAtualizacao() {
        DatabaseFirebaseRecuperar.recuperarDadosLoja(LojaInfo?.id!!, object : LojaRecuperada {
            override fun dados(Loja: Lojas?) {
                if (Loja != null) {
                    if(LojaInfo?.quantidadeAvaliacoesFila != Loja.quantidadeAvaliacoesFila){
                        NovaLoja = Loja
                    }

                } else {
                    if(referencia != null){
                        Alertas.criarAlerter(referencia!!, R.string.ErroEnviar, R.string.Atencao, 5000)
                    }
                }
            }
        })
    }

    private fun mostrarAlerta() {
        val Posicao = ProgressoFila?.progresso!!
        if(referencia != null){
            Alertas.criarAlerter(
                referencia!!,
                "${getString(R.string.IntervaloFila)}: " + "${ when {
                    Posicao < 14.2 -> getString(R.string.MenorQue10)
                    Posicao >= 14.2 && Posicao < 28.4  -> getString(R.string.Entre10e20)
                    Posicao >= 28.4 && Posicao < 42.6 -> getString(R.string.Entre20e30)
                    Posicao >= 42.6 && Posicao < 56.8 -> getString(R.string.Entre30e40)
                    Posicao >= 56.8 && Posicao < 71.0 -> getString(R.string.Entre40e50)
                    Posicao >= 71.0 && Posicao < 85.2 -> getString(R.string.Entre40e50)
                    else -> getString(R.string.Maiorque60)
                }
                } " + "${getString(R.string.ParaFila)} " +
                        Tabs?.getTabAt(Tabs?.selectedTabPosition!!)?.text.toString(),
                R.string.Atencao, 5000
            ).show()
        }
    }

    private fun configurandoAdapter() {
        adaptador = AdaptadorFilas(this.context!!, LojaInfo!!)
        Lista?.adapter = adaptador
        val Manager = LinearLayoutManager(this.context!!)
        Manager.orientation = LinearLayoutManager.HORIZONTAL
        Lista?.layoutManager = Manager
    }

}