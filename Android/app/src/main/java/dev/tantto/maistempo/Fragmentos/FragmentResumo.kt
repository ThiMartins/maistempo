package dev.tantto.maistempo.Fragmentos

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.tantto.maistempo.Adaptadores.AdaptadorFila
import dev.tantto.maistempo.Classes.AdicionarFila
import dev.tantto.maistempo.Modelos.Lojas
import dev.tantto.maistempo.R
import dev.tantto.maistempo.Telas.TelaResumo

class FragmentResumo : Fragment() {

    private lateinit var Referencia:TelaResumo

    private var Lista:RecyclerView? = null
    private var NumeroAvaliacoes:TextView? = null
    private var EnviarFilaMomento:TextView? = null
    private var LojaInfo:Lojas? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_fila, container, false)
    }


    override fun onResume() {
        super.onResume()
        configurandoView()
        configurandoAdapter()
    }

    fun passandoLja(Ref:Lojas, Contexto:Context, RefTela:TelaResumo){
        LojaInfo = Ref
        Referencia = RefTela
    }

    private fun configurandoView() {
        Lista = this.view?.findViewById<RecyclerView>(R.id.ListaFila)
        NumeroAvaliacoes = this.view?.findViewById<TextView>(R.id.NumeroAvaliacoes)
        EnviarFilaMomento = this.view?.findViewById<TextView>(R.id.FilaTexto)

        EnviarFilaMomento?.setOnClickListener {
            val Alerta = AdicionarFila().criarTela(this.context!!)
            Alerta.showTela()
            Alerta.botaoCancelar()?.setOnClickListener {
                Alerta.fecharTela()
            }
        }

        /*EnviarNota?.setOnClickListener {
            val email = FirebaseAutenticacao.Autenticacao.currentUser?.email
            if(!email.isNullOrEmpty()){
                /*Alertas.criarAlerter(
                    Referencia,
                    "${getString(R.string.SuaNota)}: ${ProgressoFila?.progress.toString()} ${getString(R.string.ParaHorario)} ${Horarios?.selectedItem.toString()}",
                    R.string.Atencao, 5000).show()
                EnviarNota?.isEnabled = false*/

                DatabaseFirebaseSalvar.adicionarPontos(email, 1, TipoPontos.PONTOS_FILA)
            }
        }*/


        NumeroAvaliacoes?.text = String.format(LojaInfo?.avaliacoes.toString() + " "+ getString(R.string.Avalicoes))
    }

    private fun configurandoAdapter() {
        Lista?.adapter = AdaptadorFila(this.context!!, LojaInfo!!)
        val Manager = LinearLayoutManager(this.context!!)
        Manager.orientation = LinearLayoutManager.HORIZONTAL
        Lista?.layoutManager = Manager
    }

}