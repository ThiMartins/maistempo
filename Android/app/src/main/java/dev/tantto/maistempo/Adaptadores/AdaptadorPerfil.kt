package dev.tantto.maistempo.Adaptadores

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import dev.tantto.maistempo.Google.FirebaseAutenticacao
import dev.tantto.maistempo.ListaPerfil
import dev.tantto.maistempo.Modelos.Geral
import dev.tantto.maistempo.Modelos.Tipo
import dev.tantto.maistempo.R
import dev.tantto.maistempo.Telas.TelaGeral
import dev.tantto.maistempo.Telas.TelaLogin
import dev.tantto.maistempo.Telas.TelaRanking
import dev.tantto.maistempo.Telas.TelaTermos

class AdaptadorPerfil(private val Contexto: Context, private val lista:List<Geral>) : RecyclerView.Adapter<AdaptadorPerfil.Holder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(Contexto).inflate(R.layout.celula_perfil, parent, false))
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.SetandoItens()
        holder.AdicionandoValores(lista[position])
        holder.Card?.setOnClickListener {
            val Modo = ListaPerfil().Recuperar(position).Modo
            val Iniciar:Intent
            if(Modo == Tipo.SAIR){
                FirebaseAutenticacao.deslogarUser()
            }
            Iniciar = when (Modo) {
                Tipo.GERAL -> Intent(Contexto, TelaGeral::class.java)
                Tipo.RANKING -> Intent(Contexto, TelaRanking::class.java)
                Tipo.TERMOS -> Intent(Contexto, TelaTermos::class.java)
                else -> Intent(Contexto, TelaLogin::class.java)
            }
            Contexto.startActivity(Iniciar)
        }
    }

    class Holder(private val Item: View) : RecyclerView.ViewHolder(Item) {

        private var Titulo: TextView? = null
        private var Imagem: ImageView? = null
        var Card:CardView? = null

        fun SetandoItens() {
            Titulo = Item.findViewById<TextView>(R.id.TituloConfiguracao)
            Imagem = Item.findViewById<ImageView>(R.id.ImagemConfiguracao)
            Card = Item.findViewById<CardView>(R.id.CardPerfil)
        }

        fun AdicionandoValores(elementos: Geral) {
            Titulo?.text = elementos.Titulo
            Imagem?.setImageResource(elementos.Imagem)
        }

    }
}
