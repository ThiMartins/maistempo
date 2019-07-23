package dev.tantto.maistempo.Fragmentos

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.tantto.maistempo.Adaptadores.AdaptadorFila
import dev.tantto.maistempo.Classes.Alertas
import dev.tantto.maistempo.Google.DatabaseFirebaseSalvar
import dev.tantto.maistempo.Google.FirebaseAutenticacao
import dev.tantto.maistempo.Google.TipoPontos
import dev.tantto.maistempo.Modelos.Lojas
import dev.tantto.maistempo.R
import dev.tantto.maistempo.Telas.TelaResumo

class FragmentResumo : Fragment() {

    private lateinit var Contexto:Context
    private lateinit var Referencia:TelaResumo

    private var Lista:RecyclerView? = null
    private var ProgressoFila:ProgressBar? = null
    private var NumeroAvaliacoes:TextView? = null
    private var LojaInfo:Lojas? = null
    private var Enviar:Button? = null
    private var Horarios:Spinner? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val V = inflater.inflate(R.layout.fragment_fila, container, false)
        ConfigurandoView(V)
        ConfigurandoAdapter()
        return V
    }

    fun PassandoLja(Ref:Lojas, Contexto:Context, RefTela:TelaResumo){
        LojaInfo = Ref
        this.Contexto = Contexto
        Referencia = RefTela
    }

    private fun ConfigurandoView(V: View) {
        Lista = V.findViewById<RecyclerView>(R.id.ListaFila)
        ProgressoFila = V.findViewById<ProgressBar>(R.id.ValorMomentoFila)
        NumeroAvaliacoes = V.findViewById<TextView>(R.id.NumeroAvaliacoes)
        Enviar = V.findViewById<Button>(R.id.EnviarFila)
        Horarios = V.findViewById<Spinner>(R.id.HorarioFilaAvaliacao)

        Enviar?.setOnClickListener {
            val email = FirebaseAutenticacao.Autenticacao.currentUser?.email
            if(!email.isNullOrEmpty()){
                Alertas.CriarTela(
                    Referencia,
                    "${getString(R.string.SuaNota)}: ${ProgressoFila?.progress.toString()} ${getString(R.string.ParaHorario)} ${Horarios?.selectedItem.toString()}",
                    R.string.Atencao, 5000).show()
                Enviar?.isEnabled = false
                DatabaseFirebaseSalvar.adicionarPontos(email, 1, TipoPontos.PONTOS_FILA)
            }
        }

        NumeroAvaliacoes?.text = String.format(LojaInfo?.avaliacoes.toString() + " "+ getString(R.string.Avalicoes))
    }

    private fun ConfigurandoAdapter() {
        Lista?.adapter = AdaptadorFila(Contexto, LojaInfo!!)
        val Manager = LinearLayoutManager(Contexto)
        Manager.orientation = LinearLayoutManager.HORIZONTAL
        Lista?.layoutManager = Manager
    }

}