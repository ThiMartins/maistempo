package dev.tantto.maistempo.telas

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.textfield.TextInputLayout
import dev.tantto.maistempo.classes.*
import dev.tantto.maistempo.google.*
import dev.tantto.maistempo.modelos.Perfil
import dev.tantto.maistempo.R
import java.io.IOException

class TelaPerfil : AppCompatActivity(), DatabasePessoaInterface, DownloadFotoCloud, DatabaseMudanca{

    private val MODO_CAMERA = 0
    private val MODO_GALERIA = 1
    private val MODO_ARQUIVOS = 2

    private var CaminhoFoto: Uri? = Uri.EMPTY

    private var Foto:ImageView? = null
    private var Nome:EditText? = null
    private var Cidade:Spinner? = null
    private var PontosCadastro:TextView? = null
    private var PontosAvaliacaoFila:TextView? = null
    private var PontosLocais:TextView? = null
    private var PontosTotal:TextView? = null
    private var Pessoa:Perfil? = null
    private var Senha:EditText? = null
    private var Modo:Boolean = false
    private var SenhaInput:TextInputLayout? = null

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
        PontosCadastro?.text = String.format(Pessoa.pontosCadastro.toString() + getString(R.string.Pontos))
        PontosAvaliacaoFila?.text = String.format(Pessoa.pontosFila.toString() + getString(R.string.Pontos))
        PontosLocais?.text = String.format(Pessoa.pontosLocais.toString() + getString(R.string.Pontos))
        PontosTotal?.text = String.format(Pessoa.pontosTotais.toString() + getString(R.string.Pontos))
    }

    private fun configurandoView() {
        Foto = findViewById(R.id.FotoPerfil)
        Nome = findViewById(R.id.NomePerfil)
        Cidade = findViewById(R.id.CidadePerfil)
        PontosCadastro = findViewById(R.id.CadastroPontos)
        PontosAvaliacaoFila = findViewById(R.id.AvaliacaoFilaPontos)
        PontosLocais = findViewById(R.id.AvaliacaoLocalPontos)
        PontosTotal = findViewById(R.id.TotalPontos)
        Senha = findViewById(R.id.SenhaNova)
        SenhaInput = findViewById(R.id.SenhaNovaInput)

        SenhaInput?.hint = getString(R.string.NovaSenhaOpcional)

        Foto?.setOnClickListener {
            exibirCaixa()
        }

        val carregar = Alertas.alertaCarregando(this)
        carregar.show()

        DatabaseFirebaseRecuperar.recuperarCidades(object : CidadesRecuperadas{
            override fun listaCidades(Lista: List<String>?) {
                carregar.dismiss()
                if(Lista != null){
                    val adapter = ArrayAdapter(this@TelaPerfil, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, Lista)
                    Cidade?.adapter = adapter

                    Cidade?.setSelection(adapter.getPosition(Pessoa?.cidade!!))
                }
            }
        })

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
                    if(Permissao.veficarPermissao(this, Permissao.Permissoes.CAMERA) == Permissao.TipoDePermissao.PERMITIDO){
                        Iniciar.action = MediaStore.ACTION_IMAGE_CAPTURE
                        startActivityForResult(Iniciar, MODO_CAMERA)
                    } else {
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 2)
                    }
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
        return super.onOptionsItemSelected(item!!)
    }

    private fun deletarConta(){
        val Aletar = Alertas.criarAlertDialog(this, R.string.ApagarConta, R.string.Atencao)
        Aletar.setNegativeButton(R.string.Nao) { _, _ ->

        }
        Aletar.setPositiveButton(R.string.Sim) { _, _ ->
            if(Pessoa != null){
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



        if(Cidade?.selectedItem?.toString() != Pessoa?.cidade){
            Alertas.criarAlerter(this, R.string.SalvandoAlteracoes, R.string.Aguardando).show()
            DatabaseFirebaseSalvar.mudarCidade(Pessoa?.email!!, Cidade?.selectedItem?.toString()!!)
        }

        if(Senha?.text?.toString()?.isNotEmpty()!! && !Modo && Senha?.text?.toString() != getString(R.string.NovaSenha)){
            var SenhaNova = ""
            var SenhaNovaConfirmada = ""
            var AlertaFechado:AlertDialog? = null
            var InputSenhaVerificar:TextInputLayout? = null
            val SenhaConfirmar:EditText?
            var SenhaAntiga:EditText? = null

            val Alerta = AlertDialog.Builder(this)
            Alerta.setView(R.layout.nova_senha)
            Alerta.setPositiveButton(R.string.Ok) { _, _ ->
                if(SenhaNova == SenhaNovaConfirmada){
                    val Email = FirebaseAutenticacao.Autenticacao.currentUser?.email
                    val VelhaSenha = SenhaAntiga?.text?.toString()
                    if(!VelhaSenha.isNullOrEmpty() && !Email.isNullOrEmpty()){
                        salvarSenha(SenhaNova, Email, VelhaSenha)
                    }
                } else {
                    InputSenhaVerificar?.error = getString(R.string.SenhaErrada)
                }
            }
            Alerta.setNegativeButton(R.string.Nao) { _, _ ->
                AlertaFechado?.dismiss()
            }
            AlertaFechado = Alerta.create()
            AlertaFechado?.setTitle(R.string.SenhaConfirmar)
            AlertaFechado?.show()
            SenhaConfirmar = AlertaFechado.findViewById(R.id.SenhaNovaConfirmacao)
            InputSenhaVerificar = AlertaFechado.findViewById(R.id.SenhaNovaVerificarInput)
            SenhaAntiga = AlertaFechado.findViewById(R.id.SenhaVelha)
            val SenhaVerificar = AlertaFechado.findViewById<EditText>(R.id.SenhaNovaVerificar)

            SenhaConfirmar?.setText(Senha?.text?.toString())

            SenhaConfirmar?.addTextChangedListener(object : TextWatcher{
                override fun afterTextChanged(p0: Editable?) {
                    if(p0 != null){
                        SenhaNova = p0.toString()
                        if(p0.toString() != SenhaVerificar?.text?.toString()){
                            InputSenhaVerificar?.error = getString(R.string.SenhaErrada)
                        } else {
                            InputSenhaVerificar?.isErrorEnabled = false
                        }
                    }
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if(p0 != null){
                        SenhaNova = p0.toString()
                        if(p0 != SenhaVerificar?.text?.toString()){
                            InputSenhaVerificar?.error = getString(R.string.SenhaErrada)
                        } else {
                            InputSenhaVerificar?.isErrorEnabled = false
                        }
                    }
                }
            })

            SenhaVerificar?.addTextChangedListener(object : TextWatcher{
                override fun afterTextChanged(p0: Editable?) {
                    if(p0 != null){
                        SenhaNovaConfirmada = p0.toString()
                        if(p0.toString() != SenhaConfirmar?.text?.toString()){
                            InputSenhaVerificar?.error = getString(R.string.SenhaErrada)
                        } else {
                            InputSenhaVerificar?.isErrorEnabled = false
                        }
                    }
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
            })
        }
    }

    private fun salvarSenha(Senha:String, Email:String, SenhaAntiga: String) {
        if (Senha.length >= 6) {
            Alertas.criarAlerter(this, R.string.SalvandoAlteracoes, R.string.Aguardando, 5000).show()
            FirebaseAutenticacao.mudarSenha(Senha , Email, SenhaAntiga, object : FirebaseAutenticacao.Mudanca {
                override fun resultado(Modo: Boolean) {
                    if (Modo) {
                        this@TelaPerfil.Modo = true
                        Alertas.criarAlerter(this@TelaPerfil, R.string.SenhaSalva, R.string.Aguardando, 5000).show()
                    } else {
                        Alertas.criarAlerter(this@TelaPerfil, R.string.ErroSenhaMudanca, R.string.Aguardando, 5000).show()
                    }
                }
            })
        } else {
            Alertas.criarAlerter(this, R.string.ErroSenhaMenor, R.string.Atencao, 5000).show()
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
            try {
                CaminhoFoto = BitmapUtilitarios.getImageUri(FotoSelecionada, Pessoa?.email!!, this)
            } catch (Erro:IOException){
                Erro.printStackTrace()
            }
            Foto?.setImageBitmap(FotoSelecionada)

        } else if(requestCode == MODO_GALERIA && resultCode == Activity.RESULT_OK && data != null){
            CaminhoFoto = data.data
            Foto?.setImageURI(CaminhoFoto)

        } else if(requestCode == MODO_ARQUIVOS && resultCode == Activity.RESULT_OK && data != null){
            CaminhoFoto = data.data
            Foto?.setImageURI(CaminhoFoto)
        }
    }

    override fun imagemBaixada(Imagem: HashMap<String, Bitmap>?) {
        if(Imagem != null){
            Foto?.setImageBitmap(Imagem.values.toList()[0])

        } else {
            Foto?.setBackgroundResource(R.drawable.borda_imagem_cinza)
            Foto?.setImageResource(R.drawable.person_dark_gray)
        }
        findViewById<ProgressBar>(R.id.CaregandoImagem).visibility = ProgressBar.INVISIBLE
    }

    override fun pessoaRecebida(Pessoa: Perfil) {
        setandoValores(Pessoa)
        this.Pessoa = Pessoa
        CloudStorageFirebase().donwloadCloud(Pessoa.email, TipoDonwload.PERFIl, this)
    }
}