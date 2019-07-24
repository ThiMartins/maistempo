package dev.tantto.maistempo.Telas

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import dev.tantto.maistempo.Google.*
import dev.tantto.maistempo.Modelos.Perfil
import dev.tantto.maistempo.R

class TelaGeral : AppCompatActivity(), DatabasePessoaInterface, DownloadFotoCloud{

    private val MODO_CAMERA = 0
    private val MODO_GALERIA = 1
    private val MODO_ARQUIVOS = 2

    private var CaminhoFoto: Uri? = null

    private var Foto:ImageView? = null
    private var Nome:EditText? = null
    private var RaioPesquisa:ProgressBar? = null
    private var PontosCadastro:TextView? = null
    private var PontosAvaliacaoFila:TextView? = null
    private var PontosLocais:TextView? = null
    private var PontosTotal:TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_geral)
        configurandoView()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        DatabaseFirebaseRecuperar.recuperaDadosPessoa(FirebaseAutenticacao.Autenticacao.currentUser?.email!!, this)
    }

    private fun setandoValores(Pessoa:Perfil){
        Nome?.setText(Pessoa.titulo)
        RaioPesquisa?.progress = Pessoa.raio.toInt()
        PontosCadastro?.text = Pessoa.pontosCadastro.toString()
        PontosAvaliacaoFila?.text = Pessoa.pontosFila.toString()
        PontosLocais?.text = Pessoa.pontosLocais.toString()
        PontosTotal?.text = Pessoa.pontosTotais.toString()
    }

    private fun configurandoView() {
        Foto = findViewById(R.id.FotoPerfil)
        Nome = findViewById(R.id.NomePerfil)
        RaioPesquisa = findViewById<ProgressBar>(R.id.DistanciaDesejada)
        PontosCadastro = findViewById(R.id.CadastroPontos)
        PontosAvaliacaoFila = findViewById(R.id.AvaliacaoFilaPontos)
        PontosLocais = findViewById(R.id.AvaliacaoLocalPontos)
        PontosTotal = findViewById(R.id.TotalPontos)

        Foto?.setOnClickListener {
            exibirCaixa()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Foto?.clipToOutline = true
        }

    }

    private fun exibirCaixa() {
        val Caixa = AlertDialog.Builder(this)
        Caixa.setTitle(R.string.Escolha)
        Caixa.setItems(arrayOf("Camera", "Galeria", "Arquivos")) { _, which ->
            val Iniciar = Intent()
            when (which) {
                0 -> {
                    Iniciar.action = MediaStore.ACTION_IMAGE_CAPTURE
                    startActivityForResult(Iniciar, MODO_CAMERA)
                }
                1 -> {
                    Iniciar.type = "image/*"
                    Iniciar.action = Intent.ACTION_PICK
                    startActivityForResult(Iniciar, MODO_GALERIA)
                }
                2 -> {
                    Iniciar.type = "image/*"
                    Iniciar.action = Intent.ACTION_GET_CONTENT
                    startActivityForResult(Iniciar, MODO_ARQUIVOS)
                }
            }
        }
        Caixa.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_geral, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val Id = item?.itemId
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == MODO_CAMERA && resultCode == Activity.RESULT_OK && data != null){
            val FotoSelecionada = data.extras?.get("data") as Bitmap
            CaminhoFoto = data.data
            Foto?.setImageBitmap(FotoSelecionada)
            //Salvar foto

        } else if(requestCode == MODO_GALERIA && resultCode == Activity.RESULT_OK && data != null){
            CaminhoFoto = data.data
            Foto?.setImageURI(CaminhoFoto)
            //Salvar foto

        } else if(requestCode == MODO_ARQUIVOS && resultCode == Activity.RESULT_OK && data != null){
            CaminhoFoto = data.data
            Foto?.setImageURI(CaminhoFoto)
            //Salvar foto
        }
    }

    override fun ImagemBaixada(Imagem: Bitmap) {
        Foto?.setImageBitmap(Imagem)
        findViewById<ProgressBar>(R.id.CaregandoImagem).visibility = ProgressBar.INVISIBLE
    }

    override fun pessoaRecebida(Pessoa: Perfil) {
        setandoValores(Pessoa)
        CloudStorageFirebase().DonwloadCloud(Pessoa.email, TipoDonwload.PERFIl, this)
    }
}