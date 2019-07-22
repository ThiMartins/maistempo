package dev.tantto.maistempo.Fragmentos

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dev.tantto.maistempo.Adaptadores.AdaptadorFila
import dev.tantto.maistempo.Modelos.Lojas
import dev.tantto.maistempo.R

class FragmentResumo(private var Contexto:Context) : Fragment() {

    private var Lista:RecyclerView? = null
    private var ProgressoFila:ProgressBar? = null
    private var NumeroAvaliacoes:TextView? = null
    private var LojaInfo:Lojas? = null
    private var Enviar:Button? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val V = LayoutInflater.from(Contexto).inflate(R.layout.fragment_fila, container, false)
        ConfigurandoView(V)
        ConfigurandoAdapter()
        return V
    }

    fun PassandoLja(Ref:Lojas){
        LojaInfo = Ref
    }

    private fun ConfigurandoView(V: View) {
        Lista = V.findViewById<RecyclerView>(R.id.ListaFila)
        ProgressoFila = V.findViewById<ProgressBar>(R.id.ValorMomentoFila)
        NumeroAvaliacoes = V.findViewById<TextView>(R.id.NumeroAvaliacoes)
        Enviar = V.findViewById<Button>(R.id.EnviarFila)

        Enviar?.setOnClickListener {
            Snackbar.make(it, ProgressoFila?.progress.toString(), Snackbar.LENGTH_SHORT).show()
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