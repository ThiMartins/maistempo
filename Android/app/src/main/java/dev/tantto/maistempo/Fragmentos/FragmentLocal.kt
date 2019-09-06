package dev.tantto.maistempo.fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dev.tantto.maistempo.adaptadores.AdaptadorLocais
import dev.tantto.maistempo.R
import dev.tantto.maistempo.telas.TelaPrincipal

class FragmentLocal: Fragment() {

    private var adaptador:AdaptadorLocais? = null
    private var swipe:SwipeRefreshLayout? = null
    private var Referencia:TelaPrincipal? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val V = inflater.inflate(R.layout.fragment_local, container, false)
        val Lista = V.findViewById<RecyclerView>(R.id.ListaLocal)
        configurandoView(V)
        adaptador = AdaptadorLocais(this.context!!)
        Lista.adapter = adaptador
        return V
    }

    fun filtro(Valor:String){
        adaptador?.filtro(Valor)
    }

    fun notificarMudanca(){
        swipe?.isRefreshing = false
        adaptador?.notifyDataSetChanged()
    }

    fun atualizando(){
        swipe?.isRefreshing = true
    }

    fun passandoReferencia(Passando:TelaPrincipal){
        Referencia = Passando
    }

    fun cancelarAtualizacao(){
        if(swipe?.isRefreshing!!){
            swipe?.isRefreshing = false
        }
    }

    private fun configurandoView(view:View){
        swipe = view.findViewById(R.id.AtualizarLista)
        swipe?.setOnRefreshListener {
            if(Referencia != null){
                Referencia?.atualizarLista()
            }
        }
    }
}