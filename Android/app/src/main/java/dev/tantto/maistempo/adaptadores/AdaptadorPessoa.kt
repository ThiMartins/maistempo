package dev.tantto.maistempo.adaptadores

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import dev.tantto.maistempo.google.FirebaseAutenticacao
import dev.tantto.maistempo.ListaPerfil
import dev.tantto.maistempo.Modelos.Geral
import dev.tantto.maistempo.Modelos.Tipo
import dev.tantto.maistempo.R
import dev.tantto.maistempo.telas.TelaPerfil
import dev.tantto.maistempo.telas.TelaLogin
import dev.tantto.maistempo.telas.TelaRanking
import dev.tantto.maistempo.telas.TelaTermo

class AdaptadorPessoa(private val Contexto: Context, private val lista:List<Geral>) : RecyclerView.Adapter<AdaptadorPessoa.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.celula_perfil, parent, false))
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setandoItens()
        holder.adicionandoValores(lista[position])
        holder.card?.setOnClickListener {
            val Modo = ListaPerfil().recuperar(position).Modo
            if(Modo == Tipo.SAIR){
                FirebaseAutenticacao.deslogarUser()
            }
            iniciarTela(Modo)
        }
    }

    private fun iniciarTela(Modo: Tipo) {
        val Iniciar:Intent = when (Modo) {
            Tipo.GERAL -> Intent(Contexto, TelaPerfil::class.java)
            Tipo.RANKING -> Intent(Contexto, TelaRanking::class.java)
            Tipo.TERMOS -> Intent(Contexto, TelaTermo::class.java)
            else -> {
                FirebaseAutenticacao.deslogarUser()
                val iniciar = Intent(Contexto, TelaLogin::class.java)
                iniciar.putExtra("Fechar", "Fechar")
            }
        }
        Contexto.startActivity(Iniciar)

    }

    class Holder(private val Item: View) : RecyclerView.ViewHolder(Item) {

        private var Titulo: TextView? = null
        private var Imagem: ImageView? = null
        var card:CardView? = null

        fun setandoItens() {
            Titulo = Item.findViewById<TextView>(R.id.TituloConfiguracao)
            Imagem = Item.findViewById<ImageView>(R.id.ImagemConfiguracao)
            card = Item.findViewById<CardView>(R.id.CardPerfil)
        }

        fun adicionandoValores(elementos: Geral) {
            Titulo?.text = elementos.Titulo
            Imagem?.setImageResource(elementos.Imagem)
        }

    }
}