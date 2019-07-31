package dev.tantto.maistempo.Fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import dev.tantto.maistempo.Adaptadores.AdaptadorLocal
import dev.tantto.maistempo.R

class FragmentLocal: Fragment() {

    private var Adaptador:AdaptadorLocal? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val V = inflater.inflate(R.layout.fragment_local_perfil, container, false)
        val Lista = V.findViewById<RecyclerView>(R.id.Lista)
        Adaptador = AdaptadorLocal(this.context!!)
        Lista.adapter = Adaptador
        return V
    }

    fun filtro(Valor:String){
        Adaptador?.filtro(Valor)
    }

}
