package dev.tantto.maistempo.Fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import dev.tantto.maistempo.Adaptadores.AdaptadorFavoritos
import dev.tantto.maistempo.ListaFavoritos
import dev.tantto.maistempo.R

class FragmentFavoritos: Fragment() {

    private var Adaptador: AdaptadorFavoritos? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val V = inflater.inflate(R.layout.fragment_local_perfil, container, false)
        val Lista = V.findViewById<RecyclerView>(R.id.Lista)
        Adaptador = AdaptadorFavoritos(this.context!!, ListaFavoritos.recuperarFavoritos())
        Lista.adapter = Adaptador
        return V
    }

    fun filtro(Valor:String){
        Adaptador?.filtro(Valor)
    }

    fun tamanhoLista() :Int{
        return Adaptador?.itemCount!!
    }

    fun reloadLista(){
        Adaptador?.reloadData()
    }

}