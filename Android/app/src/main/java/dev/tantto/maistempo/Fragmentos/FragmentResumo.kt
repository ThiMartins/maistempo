package dev.tantto.maistempo.Fragmentos

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.tantto.maistempo.Adaptadores.AdaptadorFila
import dev.tantto.maistempo.R

class FragmentResumo(private var Contexto:Context) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val V = LayoutInflater.from(Contexto).inflate(R.layout.fragment_fila, container, false)
        val Lista = V.findViewById<RecyclerView>(R.id.ListaFila)
        Lista.adapter = AdaptadorFila(Contexto, listOf())
        val Manager = LinearLayoutManager(Contexto)
        Manager.orientation = LinearLayoutManager.HORIZONTAL
        Lista.layoutManager = Manager
        return V
    }

}