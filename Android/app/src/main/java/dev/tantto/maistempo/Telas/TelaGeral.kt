package dev.tantto.maistempo.Telas

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import dev.tantto.maistempo.Google.*
import dev.tantto.maistempo.Modelos.Perfil
import dev.tantto.maistempo.R

class TelaGeral : AppCompatActivity(), DatabasePessoaInterface, DownloadFotoCloud{

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
        configurandoView()
        DatabaseFirebaseRecuperar.RecuperaDadosPessoa(FirebaseAutenticacao.Autenticacao.currentUser?.email!!, this)
    }

    private fun setandoValores(Pessoa:Perfil){
        Nome?.text = Pessoa.titulo
        RaioPesquisa?.progress = Pessoa.raio.toInt()
        PontosCadastro?.text = Pessoa.pontosCadastro
        PontosAvaliacaoFila?.text = Pessoa.pontosFila
        PontosLocais?.text = Pessoa.pontosLocais
        PontosTotal?.text = Pessoa.pontosTotais
    }

    private fun configurandoView() {
        Foto = findViewById(R.id.FotoPerfil)
        Nome = findViewById(R.id.NomePerfil)
        RaioPesquisa = findViewById<ProgressBar>(R.id.DistanciaDesejada)
        PontosCadastro = findViewById(R.id.CadastroPontos)
        PontosAvaliacaoFila = findViewById(R.id.AvaliacaoFilaPontos)
        PontosLocais = findViewById(R.id.AvaliacaoLocalPontos)
        PontosTotal = findViewById(R.id.TotalPontos)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Foto?.clipToOutline = true
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_feedback, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val Id = item?.itemId
        return super.onOptionsItemSelected(item)
    }

    override fun ImagemBaixada(Imagem: Bitmap) {
        Foto?.setImageBitmap(Imagem)
        findViewById<ProgressBar>(R.id.CaregandoImagem).visibility = ProgressBar.INVISIBLE
    }

    override fun PessoaRecebida(Pessoa: Perfil) {
        setandoValores(Pessoa)
        CloudStorageFirebase().DonwloadCloud(Pessoa.email, TipoDonwload.PERFIl, this)
    }
}