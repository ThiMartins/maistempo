package dev.tantto.maistempo.fragmentos

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dev.tantto.maistempo.classes.BuscarLojasImagem
import dev.tantto.maistempo.ListaBitmap
import dev.tantto.maistempo.ListaLocais
import dev.tantto.maistempo.modelos.Lojas
import dev.tantto.maistempo.modelos.Perfil
import dev.tantto.maistempo.adaptadores.AdaptadorLocais
import dev.tantto.maistempo.R
import dev.tantto.maistempo.google.FirebaseAutenticacao

class FragmentLocal: Fragment() {

    var adaptador:AdaptadorLocais? = null
    private var Swipe:SwipeRefreshLayout? = null

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
        adaptador?.notifyDataSetChanged()
    }

    private fun configurandoView(view:View){
        Swipe = view.findViewById(R.id.AtualizarLista)
        Swipe?.setOnRefreshListener {

            BuscarLojasImagem(FirebaseAutenticacao.Autenticacao.currentUser?.email!!, object : BuscarLojasImagem.BuscarConcluida {
                override fun concluido(Modo: Boolean, Lista: MutableList<Lojas>?, ListaImagem: HashMap<String, Bitmap>?, Pessoa: Perfil?) {
                    Swipe?.isRefreshing = false
                    if(Lista != null  && ListaImagem != null){
                        ListaLocais.refazer(Lista)
                        ListaBitmap.refazer(ListaImagem)
                        notificarMudanca()
                    }
                }
            })
        }

    }

}