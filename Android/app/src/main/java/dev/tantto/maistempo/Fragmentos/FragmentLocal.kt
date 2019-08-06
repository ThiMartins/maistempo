package dev.tantto.maistempo.Fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import dev.tantto.maistempo.adaptadores.AdaptadorLocais
import dev.tantto.maistempo.R

class FragmentLocal: Fragment() {

    private var adaptador:AdaptadorLocais? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val V = inflater.inflate(R.layout.fragment_local_perfil, container, false)
        val Lista = V.findViewById<RecyclerView>(R.id.Lista)
        adaptador = AdaptadorLocais(this.context!!)
        Lista.adapter = adaptador
        return V
    }

    fun filtro(Valor:String){
        adaptador?.filtro(Valor)
    }

    fun notificarMudanca(){
        adaptador?.notifyDataSetChanged()
    }

}
