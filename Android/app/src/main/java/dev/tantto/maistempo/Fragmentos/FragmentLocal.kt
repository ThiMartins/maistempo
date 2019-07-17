package dev.tantto.maistempo.Fragmentos

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import dev.tantto.maistempo.Adaptadores.AdaptadorLocal
import dev.tantto.maistempo.Modelos.Lojas
import dev.tantto.maistempo.R

class FragmentLocal(private val Contexto: Context, private val ListaElementos:List<Lojas>) : Fragment() {

    private var Adaptador:AdaptadorLocal? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val V = LayoutInflater.from(Contexto).inflate(R.layout.fragment_local_perfil, container, false)
        val Lista = V.findViewById<RecyclerView>(R.id.Lista)
        Adaptador = AdaptadorLocal(Contexto, ListaElementos)
        Lista.adapter = Adaptador
        return V
    }

    fun Filtro(Valor:String){
        Adaptador?.Filtro(Valor)
    }

}
