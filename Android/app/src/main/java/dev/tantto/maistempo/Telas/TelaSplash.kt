package dev.tantto.maistempo.telas

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dev.tantto.maistempo.Classes.*
import dev.tantto.maistempo.google.*
import dev.tantto.maistempo.ListaBitmap
import dev.tantto.maistempo.ListaLocais
import dev.tantto.maistempo.Modelos.Lojas
import dev.tantto.maistempo.Modelos.Perfil
import dev.tantto.maistempo.R
import dev.tantto.maistempo.chaves.Chave

class TelaSplash : AppCompatActivity(), BuscarLojasImagem.BuscarConcluida {

    private val RequisicaoPermissaoCamera = 0
    private val RequisicaoPermissaoLeitura = 1
    private val RequisicaoPermissaoEscrita = 2
    private val RequisicaoPermissaoFine = 3
    private val RequisicaoPermissaoCoarse = 4
    private val handler = Handler(Looper.getMainLooper())
    private var Iniciado = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_splash)
        supportActionBar?.elevation = 0F

    }

    override fun onResume() {
        super.onResume()

        when {
            Permissao.veficarPermissao(this, Permissoes.CAMERA) != TipoDePermissao.PERMITIDO -> ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), RequisicaoPermissaoCamera)
            Permissao.veficarPermissao(this, Permissoes.ARMAZENAMENTO_READ) != TipoDePermissao.PERMITIDO -> ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), RequisicaoPermissaoLeitura)
            Permissao.veficarPermissao(this, Permissoes.ARMAZENAMENTO_WRITE) != TipoDePermissao.PERMITIDO -> ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), RequisicaoPermissaoEscrita)
            Permissao.veficarPermissao(this, Permissoes.LOCALIZACAO_COARSE) != TipoDePermissao.PERMITIDO -> ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), RequisicaoPermissaoCoarse)
            Permissao.veficarPermissao(this, Permissoes.LOCALIZACAO_FINE) != TipoDePermissao.PERMITIDO -> ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), RequisicaoPermissaoFine)
            else -> iniciarVerificacoes()
        }
    }

    private fun iniciarVerificacoes(){
        if(Dados.recuperarLogin(this)) {
            FirebaseAutenticacao.deslogarUser()
        }

        if(internetConectado() == true){
            carregandoLogin()
        } else {
            verificarInternet()
        }
    }

    private fun verificarInternet(){
        val Snack = Snackbar.make(findViewById(R.id.TelaSplash), R.string.SemConexao, Snackbar.LENGTH_INDEFINITE)
        Snack.show()
        object : Runnable{
            override fun run() {
                if(internetConectado() == false || internetConectado() == null) {
                    handler.postDelayed(this, 100)
                } else {
                    Snack.setText(R.string.Conectado)
                    Snack.duration = BaseTransientBottomBar.LENGTH_SHORT
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        Snack.view.setBackgroundColor(getColor(R.color.colorPrimary))
                    }
                    carregandoLogin()
                }
            }
        }.run()
    }

    private fun carregandoLogin() {
        val User = FirebaseAutenticacao.Autenticacao.currentUser?.email

        if (!User.isNullOrEmpty()) {
            BuscarLojasImagem(User, this)
        } else {
            iniciarActivity(Intent(this, TelaLogin::class.java))
            finishAffinity()
        }
    }

    override fun concluido(Modo: Boolean, Lista: MutableList<Lojas>?, ListaImagem: HashMap<String, Bitmap>?, Pessoa: Perfil) {
        if(Lista != null && ListaImagem != null){
            ListaLocais.refazer(Lista)
            ListaBitmap.refazer(ListaImagem)
            if(Pessoa.acesso == Chave.CHAVE_ADM.valor){
                val iniciar = Intent(this, TelaPrincipal::class.java)
                iniciar.putExtra(Chave.CHAVE_ACESSO.valor, Chave.CHAVE_ADM.valor)
                iniciarActivity(iniciar)
                finishAffinity()
            } else {
                iniciarActivity(Intent(this, TelaPrincipal::class.java))
                finishAffinity()
            }
        }
    }

    private fun iniciarActivity(Iniciar:Intent){
        if(!Iniciado){
            startActivity(Iniciar)
            Iniciado = true
        }
    }

    @Suppress("DEPRECATION")
    private fun internetConectado() : Boolean? {
        val ConexaoManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return ConexaoManager.activeNetworkInfo?.isConnected
    }

}