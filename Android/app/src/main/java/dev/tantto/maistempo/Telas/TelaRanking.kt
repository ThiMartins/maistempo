package dev.tantto.maistempo.Telas

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.tantto.maistempo.Adaptadores.AdaptadorRanking
import dev.tantto.maistempo.Google.DatabaseFirebaseRecuperar
import dev.tantto.maistempo.Google.DatabaseRakingInterface
import dev.tantto.maistempo.Modelos.Perfil
import dev.tantto.maistempo.R

class TelaRanking : AppCompatActivity(), DatabaseRakingInterface {

    private var Lista: RecyclerView? = null
    private var Adapter: AdaptadorRanking? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        DatabaseFirebaseRecuperar.recuperarTopRanking(this)

        Lista = findViewById<RecyclerView>(R.id.ListaRanking)
        Adapter = AdaptadorRanking(this)
        val Manager = LinearLayoutManager(this)
        Manager.orientation = RecyclerView.VERTICAL
        Lista?.layoutManager = Manager
        Lista?.adapter = Adapter
    }

    override fun topRanking(Lista: List<Perfil>) {
        Adapter?.adicionandoValor(Lista)
    }

}