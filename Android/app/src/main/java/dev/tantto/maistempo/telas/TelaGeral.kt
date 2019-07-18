package dev.tantto.maistempo.telas

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import dev.tantto.maistempo.Modelos.Perfil
import dev.tantto.maistempo.R

class TelaGeral : AppCompatActivity() {

    private var Foto:ImageView? = null
    private var Nome:TextView? = null
    private var RaioPesquisa:ProgressBar? = null
    private var PontosCadastro:TextView? = null
    private var PontosAvaliacaoFila:TextView? = null
    private var PontosLocais:TextView? = null
    private var PontosTotal:TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_geral)
        ConfigurandoView()
        SetandoValores(Perfil())
    }

    private fun SetandoValores(Pessoa:Perfil){
        //Foto?.setImageResource(Pessoa.Imagem)
        Nome?.text = Pessoa.Titulo
        RaioPesquisa?.progress = Pessoa.Raio
        PontosCadastro?.text = Pessoa.PontosCadastro.toString()
        PontosAvaliacaoFila?.text = Pessoa.PontosFila.toString()
        PontosLocais?.text = Pessoa.PontosLocais.toString()
        PontosTotal?.text = Pessoa.PontosTotais.toString()
    }

    private fun ConfigurandoView() {
        Foto = findViewById(R.id.FotoPerfil)
        Nome = findViewById(R.id.NomePerfil)
        RaioPesquisa = findViewById<ProgressBar>(R.id.DistanciaDesejada)
        PontosCadastro = findViewById(R.id.CadastroPontos)
        PontosAvaliacaoFila = findViewById(R.id.AvaliacaoFilaPontos)
        PontosLocais = findViewById(R.id.AvaliacaoLocalPontos)
        PontosTotal = findViewById(R.id.TotalPontos)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_feedback, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val Id = item?.itemId
        return super.onOptionsItemSelected(item)
    }

}