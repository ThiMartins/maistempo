package dev.tantto.maistempo.Fragmentos

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import dev.tantto.maistempo.Adaptadores.AdaptadorPerfil
import dev.tantto.maistempo.Modelos.Geral
import dev.tantto.maistempo.R

class FragmentPerfil (private val Contexto: Context, private val listaElementos:List<Geral>) : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val V = LayoutInflater.from(Contexto).inflate(R.layout.fragment_local_perfil, container, false)
        val Lista = V.findViewById<RecyclerView>(R.id.Lista)
        Lista.adapter = AdaptadorPerfil(Contexto, listaElementos)
        return V
    }
}