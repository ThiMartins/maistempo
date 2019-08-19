package dev.tantto.maistempo.fragmentos

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.ramotion.fluidslider.FluidSlider
import dev.tantto.maistempo.adaptadores.AdaptadorFilas
import dev.tantto.maistempo.chaves.Chave
import dev.tantto.maistempo.classes.Alertas
import dev.tantto.maistempo.google.*
import dev.tantto.maistempo.modelos.Lojas
import dev.tantto.maistempo.R
import dev.tantto.maistempo.telas.TelaResumoLoja
import java.util.*


class FragmentResumo : Fragment(), FunctionsInterface {

    private lateinit var referencia:TelaResumoLoja
    private val handler = Handler(Looper.getMainLooper())

    private var Lista:RecyclerView? = null
    private var NumeroAvaliacoes:TextView? = null
    private var EnviarFilaMomento:Button? = null
    private var ProgressoFila:FluidSlider? = null
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

        ProgressoFila?.startText = "0"
        ProgressoFila?.endText = "7"
        ProgressoFila?.position = 0.0F
        ProgressoFila?.textSize = 34F
        ProgressoFila?.bubbleText = "< 10 min"

        ProgressoFila?.positionListener = { pos ->
            val valor = pos * 100
            val value = when{
                valor < 14.28 -> "< 10 min"
                valor >= 14.28 && valor < 28.56 -> "10 a 20 min"
                valor >= 28.56 && valor < 42.84 -> "20 a 30 min"
                valor >= 42.84 && valor < 57.12 -> "30 a 40 min"
                valor >= 57.12 && valor < 71.4 -> "40 a 50 min"
                valor >= 71.4 && valor < 85.68 -> "50 a 60 min"
                else -> "> 60 min"
            }
            ProgressoFila?.bubbleText = value
        }

        EnviarFilaMomento?.setOnClickListener {
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


                val valor = ProgressoFila?.position!!
                val valorNota = when {
                    valor < 14.28 -> 0
                    valor >= 14.28 && valor < 28.56 -> 1
                    valor >= 28.56 && valor < 42.84 -> 2
                    valor >= 42.84 && valor < 57.12 -> 3
                    valor >= 57.12 && valor < 71.4 -> 4
                    valor >= 71.4 && valor < 85.68 -> 5
                    else -> 6
                }

                CloudFunctions.adicionarNotaFila(LojaInfo?.id!!, Tipo?.valor!!, valorNota.toDouble(), Horas.toString(), this)
                Alertas.criarAlerter(referencia, R.string.EnviandoNota ,R.string.Enviando, 10000).show()
            }
        }

        NumeroAvaliacoes?.text = String.format(LojaInfo?.quantidadeAvaliacoesFila.toString() + " "+ getString(R.string.Avalicoes))
    }

    private fun recuperandoView() {
        Lista = this.view?.findViewById<RecyclerView>(R.id.ListaFila)
        NumeroAvaliacoes = this.view?.findViewById<TextView>(R.id.NumeroAvaliacoes)
        Tabs = this.view?.findViewById<TabLayout>(R.id.TabLayoutFila)
        EnviarFilaMomento = this.view?.findViewById<Button>(R.id.EnviarFila)
        ProgressoFila = this.view?.findViewById<FluidSlider>(R.id.ProgressoFilaVoto)
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
        if(Valor == Resultado.ERRO){
            Alertas.criarAlerter(referencia, R.string.ErroEnviar, R.string.Atencao, 10000).show()
        } else {
            val alerta = Alertas.criarAlerter(referencia, R.string.Atualizando, R.string.Aguardando)
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
                        adaptador?.notifyDataSetChanged()
                        NovaLoja = null
                    }
                }
            }.run()
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
                    Alertas.criarAlerter(referencia, R.string.ErroEnviar, R.string.Atencao, 5000)
                }
            }
        })
    }

    private fun mostrarAlerta() {
        Alertas.criarAlerter(
            referencia,
            "${getString(R.string.IntervaloFila)}: " +
                    "${
                    when (ProgressoFila?.position?.toInt()) {
                        0 -> getString(R.string.MenorQue10)
                        1 -> getString(R.string.Entre10e20)
                        2 -> getString(R.string.Entre20e30)
                        3 -> getString(R.string.Entre30e40)
                        4 -> getString(R.string.Entre40e50)
                        5 -> getString(R.string.Entre50e60)
                        else -> getString(R.string.Maiorque60)
                    }
                    } " +
                    "${getString(R.string.ParaFila)} " +
                    Tabs?.getTabAt(Tabs?.selectedTabPosition!!)?.text.toString(),
            R.string.Atencao, 5000
        ).show()
    }

    private fun configurandoAdapter() {
        adaptador = AdaptadorFilas(this.context!!, LojaInfo!!)
        Lista?.adapter = adaptador
        val Manager = LinearLayoutManager(this.context!!)
        Manager.orientation = LinearLayoutManager.HORIZONTAL
        Lista?.layoutManager = Manager
    }

}