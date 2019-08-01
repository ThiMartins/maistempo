package dev.tantto.maistempo.Telas

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import dev.tantto.maistempo.Classes.Alertas
import dev.tantto.maistempo.Classes.bitmapUtils
import dev.tantto.maistempo.Google.*
import dev.tantto.maistempo.Modelos.Perfil
import dev.tantto.maistempo.R

class TelaGeral : AppCompatActivity(), DatabasePessoaInterface, DownloadFotoCloud, DatabaseMudanca{

    private val MODO_CAMERA = 0
    private val MODO_GALERIA = 1
    private val MODO_ARQUIVOS = 2

    private var CaminhoFoto: Uri? = Uri.EMPTY

    private var Foto:ImageView? = null
    private var Nome:EditText? = null
    private var RaioPesquisa:ProgressBar? = null
    private var PontosCadastro:TextView? = null
    private var PontosAvaliacaoFila:TextView? = null
    private var PontosLocais:TextView? = null
    private var PontosTotal:TextView? = null
    private var Pessoa:Perfil? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_geral)
        configurandoView()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 0F
        DatabaseFirebaseRecuperar.recuperaDadosPessoa(FirebaseAutenticacao.Autenticacao.currentUser?.email!!, this)
    }

    private fun setandoValores(Pessoa:Perfil){
        Nome?.setText(Pessoa.titulo)
        RaioPesquisa?.progress = Pessoa.raio.toInt()
        PontosCadastro?.text = String.format(Pessoa.pontosCadastro.toString() + getString(R.string.Pontos))
        PontosAvaliacaoFila?.text = String.format(Pessoa.pontosFila.toString() + getString(R.string.Pontos))
        PontosLocais?.text = String.format(Pessoa.pontosLocais.toString() + getString(R.string.Pontos))
        PontosTotal?.text = String.format(Pessoa.pontosTotais.toString() + getString(R.string.Pontos))
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
        Caixa.setItems(arrayOf(getString(R.string.Camera), getString(R.string.Galeria), getString(R.string.Arquivos))) { _, which ->
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
        when(item?.itemId){
            R.id.DeletarConta -> deletarConta()
            R.id.SalvarAlteracoes -> salvarAlteracoes()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deletarConta(){
        val Aletar = Alertas.criarAlertDialog(this, R.string.ApagarConta, R.string.Atencao)
        Aletar.setNegativeButton(R.string.Nao) { _, _ ->

        }
        Aletar.setPositiveButton(R.string.Sim) { _, _ ->
            if(Pessoa != null){
                FirebaseAutenticacao.deslogarUser()
                DatabaseFirebaseSalvar.deletarConta(Pessoa?.email!!)
                startActivity(Intent(this, TelaLogin::class.java))
                finishAffinity()
            }
        }.create().show()
    }

    private fun salvarAlteracoes(){
        if(Pessoa?.titulo != Nome?.text?.toString() && Nome?.text?.toString()?.isNotEmpty()!! && CaminhoFoto == Uri.EMPTY){
            Alertas.criarAlerter(this, R.string.SalvandoAlteracoes, R.string.Aguardando, 5000).show()
            DatabaseFirebaseSalvar.mudarNomeComImagem(Pessoa?.email!!, Nome?.text?.toString()!!, Uri.EMPTY , this)
        } else if(Pessoa?.titulo != Nome?.text?.toString() && Nome?.text?.toString()?.isNotEmpty()!! && CaminhoFoto != Uri.EMPTY){
            Alertas.criarAlerter(this, R.string.SalvandoAlteracoes, R.string.Aguardando).show()
            DatabaseFirebaseSalvar.mudarNomeComImagem(Pessoa?.email!!, Nome?.text?.toString()!!, CaminhoFoto!! , this)
        } else if(Pessoa?.titulo == Nome?.text?.toString() && CaminhoFoto != Uri.EMPTY){
            Alertas.criarAlerter(this, R.string.SalvandoAlteracoes, R.string.Aguardando).show()
            DatabaseFirebaseSalvar.mudarNomeComImagem(Pessoa?.email!!, "", CaminhoFoto!! , this)
        }

        if(Pessoa?.raio?.toInt() != RaioPesquisa?.progress){
            DatabaseFirebaseSalvar.mudarRaio(Pessoa?.email!!, RaioPesquisa?.progress!!)
        }
    }

    override fun resposta(Resposta: Respostas) {
        if(Resposta == Respostas.SUCESSO){
            this.finish()
        } else if(Resposta == Respostas.ERRO){
            Alertas.criarAlerter(this, R.string.ErroMudancas, R.string.Erro, 5000).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == MODO_CAMERA && resultCode == Activity.RESULT_OK && data != null){
            val FotoSelecionada = data.extras?.get("data") as Bitmap
            CaminhoFoto = bitmapUtils.getImageUri(FotoSelecionada, Pessoa?.email!!, this)
            Foto?.setImageBitmap(FotoSelecionada)

        } else if(requestCode == MODO_GALERIA && resultCode == Activity.RESULT_OK && data != null){
            CaminhoFoto = data.data
            Foto?.setImageURI(CaminhoFoto)

        } else if(requestCode == MODO_ARQUIVOS && resultCode == Activity.RESULT_OK && data != null){
            CaminhoFoto = data.data
            Foto?.setImageURI(CaminhoFoto)
        }
    }

    override fun imagemBaixada(Imagem: Bitmap?) {
        if(Imagem != null){
            Foto?.setImageBitmap(Imagem)
            findViewById<ProgressBar>(R.id.CaregandoImagem).visibility = ProgressBar.INVISIBLE
        }
    }

    override fun pessoaRecebida(Pessoa: Perfil) {
        setandoValores(Pessoa)
        this.Pessoa = Pessoa
        CloudStorageFirebase().donwloadCloud(Pessoa.email, TipoDonwload.PERFIl, this)
    }
}