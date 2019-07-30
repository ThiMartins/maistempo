package dev.tantto.maistempo.Fragmentos

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import dev.tantto.maistempo.Adaptadores.AdaptadorFila
import dev.tantto.maistempo.Chaves.Chaves
import dev.tantto.maistempo.Classes.Alertas
import dev.tantto.maistempo.Google.DatabaseFirebaseSalvar
import dev.tantto.maistempo.Google.FirebaseAutenticacao
import dev.tantto.maistempo.Google.TipoFila
import dev.tantto.maistempo.Google.TipoPontos
import dev.tantto.maistempo.Modelos.Lojas
import dev.tantto.maistempo.R
import dev.tantto.maistempo.Telas.TelaResumo
import java.util.*

class FragmentResumo : Fragment() {

    private lateinit var Referencia:TelaResumo

    private var Lista:RecyclerView? = null
    private var NumeroAvaliacoes:TextView? = null
    private var EnviarFilaMomento:Button? = null
    private var ProgressoFila:SeekBar? = null
    private var Tabs:TabLayout? = null
    private var LojaInfo:Lojas? = null
    private var Adaptador:AdaptadorFila? = null

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
        outState.putSerializable(Chaves.CHAVE_LOJA.valor, LojaInfo)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if(savedInstanceState != null && savedInstanceState.containsKey(Chaves.CHAVE_LOJA.valor)){
            LojaInfo = savedInstanceState.getSerializable(Chaves.CHAVE_LOJA.valor) as Lojas
        }
    }

    fun passandoLja(Ref:Lojas, RefTela:TelaResumo){
        LojaInfo = Ref
        Referencia = RefTela
    }

    @Suppress("DEPRECATION")
    private fun configurandoView() {
        Lista = this.view?.findViewById<RecyclerView>(R.id.ListaFila)
        NumeroAvaliacoes = this.view?.findViewById<TextView>(R.id.NumeroAvaliacoes)
        Tabs = this.view?.findViewById<TabLayout>(R.id.TabLayoutFila)
        EnviarFilaMomento = this.view?.findViewById<Button>(R.id.EnviarFila)
        ProgressoFila = this.view?.findViewById<SeekBar>(R.id.ProgressoFilaVoto)

        Tabs?.getTabAt(0)?.setText(R.string.Normal)
        Tabs?.getTabAt(1)?.setText(R.string.Rapida)
        Tabs?.getTabAt(2)?.setText(R.string.Preferencial)
        Tabs?.setOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                Adaptador?.mudarValores(Tabs?.selectedTabPosition!!)
            }
        })

        EnviarFilaMomento?.setOnClickListener {
            val email = FirebaseAutenticacao.Autenticacao.currentUser?.email
            if (!email.isNullOrEmpty()) {
                mostrarAlerta()
                //EnviarFilaMomento?.isEnabled = false
                DatabaseFirebaseSalvar.adicionarPontos(email, 1, TipoPontos.PONTOS_FILA)

                val Horas = Calendar.getInstance().get(Calendar.HOUR)
                val Tipo = when {
                    Tabs?.selectedTabPosition == 0 -> TipoFila.FilaNormal
                    Tabs?.selectedTabPosition == 1 -> TipoFila.FilaRapida
                    Tabs?.selectedTabPosition == 2 -> TipoFila.FilaPrererencial
                    else -> null
                }

                DatabaseFirebaseSalvar.adicionarNotaFila(LojaInfo?.id!!, ProgressoFila?.progress!!, Horas.toString(), Tipo!!)
            }
        }

        NumeroAvaliacoes?.text = String.format(LojaInfo?.quantidadeAvaliacoesFila.toString() + " "+ getString(R.string.Avalicoes))
    }

    private fun mostrarAlerta() {
        Alertas.criarAlerter(
            Referencia,
            "${getString(R.string.IntervaloFila)}: " +
                    "${
                    when (ProgressoFila?.progress) {
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
        Adaptador = AdaptadorFila(this.context!!, LojaInfo!!)
        Lista?.adapter = Adaptador
        val Manager = LinearLayoutManager(this.context!!)
        Manager.orientation = LinearLayoutManager.HORIZONTAL
        Lista?.layoutManager = Manager
    }

}