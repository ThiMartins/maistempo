package dev.tantto.maistempo.Telas

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.tantto.maistempo.Adaptadores.AdaptadorRanking
import dev.tantto.maistempo.Google.DatabaseFirebaseRecuperar
import dev.tantto.maistempo.Google.DatabaseRakingInterface
import dev.tantto.maistempo.Modelos.Perfil
import dev.tantto.maistempo.R

class TelaRanking : AppCompatActivity(), DatabaseRakingInterface {

    private var ListaRecycler: RecyclerView? = null
    private var Adapter: AdaptadorRanking? = null
    private var Alerta:AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 0F
        setContentView(R.layout.activity_ranking)
        DatabaseFirebaseRecuperar.recuperarTopRanking(this)

        val AlertaBuilder = AlertDialog.Builder(this)
        AlertaBuilder.setView(R.layout.loading)
        Alerta = AlertaBuilder.create()
        Alerta?.show()
    }

    override fun topRanking(Lista: List<Perfil>) {
        ListaRecycler = findViewById<RecyclerView>(R.id.ListaRanking)
        Adapter = AdaptadorRanking(this)
        val Manager = LinearLayoutManager(this)
        Manager.orientation = RecyclerView.VERTICAL
        ListaRecycler?.layoutManager = Manager
        ListaRecycler?.adapter = Adapter
        Adapter?.adicionandoValor(Lista)
        Alerta?.dismiss()
    }

}