package dev.tantto.maistempo.telas

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.functions.FirebaseFunctions
import dev.tantto.maistempo.adaptadores.AdaptadorRaking
import dev.tantto.maistempo.google.DatabaseFirebaseRecuperar
import dev.tantto.maistempo.google.DatabaseRakingInterface
import dev.tantto.maistempo.modelos.Perfil
import dev.tantto.maistempo.R
import dev.tantto.maistempo.google.DatabasePessoaInterface
import dev.tantto.maistempo.google.FirebaseAutenticacao

class TelaRanking : AppCompatActivity(), DatabaseRakingInterface, DatabasePessoaInterface {

    private var ListaRecycler: RecyclerView? = null
    private var adapter: AdaptadorRaking? = null
    private var Alerta:AlertDialog? = null
    private var Posicao:Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 0F
        supportActionBar?.setTitle(R.string.Ranking)
        setContentView(R.layout.activity_ranking)
        DatabaseFirebaseRecuperar.recuperarTopRanking(this)
        val valores = hashMapOf(Pair("email", FirebaseAutenticacao.Autenticacao.currentUser?.email!!))
        FirebaseFunctions.getInstance().getHttpsCallable("rankingPessoa").call(valores).continueWith {
            if(it.isSuccessful){
                val Retorno = it.result?.data
                if(Retorno != null){
                    Posicao = Retorno.toString().toLong()
                    DatabaseFirebaseRecuperar.recuperaDadosPessoa(FirebaseAutenticacao.Autenticacao.currentUser?.email!!, this)
                }
            }
        }
        val AlertaBuilder = AlertDialog.Builder(this)
        AlertaBuilder.setView(R.layout.loading)
        Alerta = AlertaBuilder.create()
        Alerta?.window?.setBackgroundDrawableResource(R.color.Transparente)
        Alerta?.show()
    }

    override fun topRanking(Lista: MutableList<Perfil>) {
        ListaRecycler = findViewById<RecyclerView>(R.id.ListaRanking)
        val divisor = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        ListaRecycler?.addItemDecoration(divisor)
        adapter = AdaptadorRaking()
        ListaRecycler?.adapter = adapter
        adapter?.adicionandoValor(Lista)


    }

    override fun pessoaRecebida(Pessoa: Perfil) {
        val PessoaLista = findViewById<RecyclerView>(R.id.ListaRankingPessoa)
        val NovoAdapter = AdaptadorRaking()
        PessoaLista.adapter = NovoAdapter
        NovoAdapter.adicionandoValor(listOf(Pessoa), Posicao)
        Alerta?.dismiss()
    }

}