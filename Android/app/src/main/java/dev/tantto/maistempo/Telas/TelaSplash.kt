package dev.tantto.maistempo.telas

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dev.tantto.maistempo.classes.*
import dev.tantto.maistempo.google.*
import dev.tantto.maistempo.ListaBitmap
import dev.tantto.maistempo.ListaLocais
import dev.tantto.maistempo.R
import dev.tantto.maistempo.modelos.Lojas
import dev.tantto.maistempo.modelos.Perfil
import dev.tantto.maistempo.chaves.Chave
import dev.tantto.maistempo.chaves.Requisicoes
import kotlin.collections.HashMap

class TelaSplash : AppCompatActivity(), BuscarLojasImagem.BuscarConcluida {

    private val handler = Handler(Looper.getMainLooper())
    private var Iniciado = false
    private var Pausado = false
    private var PessoaPassada:Perfil? = null
    private var TipoRequisicao = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_splash)
        supportActionBar?.elevation = 0F
    }

    override fun onPause() {
        super.onPause()
        Pausado = true
    }

    override fun onRestart() {
        super.onRestart()
        if(Pausado){
            Pausado = false
            prepararInicio(PessoaPassada)
        }
    }

    override fun onPostResume() {
        super.onPostResume()
        veficarRequisicoes()
    }

    private fun veficarRequisicoes() {
        when{
            Permissao.veficarPermissao(this, Permissao.Permissoes.CAMERA) != Permissao.TipoDePermissao.PERMITIDO && TipoRequisicao == 0 -> {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), Requisicoes.REQUISICAO_CAMERA.valor)
                TipoRequisicao = TipoRequisicao or 1
            }
            Permissao.veficarPermissao(this, Permissao.Permissoes.ARMAZENAMENTO_READ) != Permissao.TipoDePermissao.PERMITIDO && TipoRequisicao < 2 -> {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), Requisicoes.REQUISICAO_LEITURA_STORAGE.valor)
                TipoRequisicao = TipoRequisicao or 2
            }
            Permissao.veficarPermissao(this, Permissao.Permissoes.ARMAZENAMENTO_WRITE) != Permissao.TipoDePermissao.PERMITIDO && TipoRequisicao < 4 -> {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), Requisicoes.REQUISICAO_ESCRITA_STORAGE.valor)
                TipoRequisicao = TipoRequisicao or 4
            }
            Permissao.veficarPermissao(this, Permissao.Permissoes.LOCALIZACAO_COARSE) != Permissao.TipoDePermissao.PERMITIDO && TipoRequisicao < 8 -> {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), Requisicoes.REQUISICAO_COARSE_ACCESS.valor)
                TipoRequisicao = TipoRequisicao or 8
            }
            Permissao.veficarPermissao(this, Permissao.Permissoes.LOCALIZACAO_FINE) != Permissao.TipoDePermissao.PERMITIDO && TipoRequisicao < 16 -> {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), Requisicoes.REQUISICAO_FINE_ACCESS.valor)
                TipoRequisicao = TipoRequisicao or 16
            }
            else -> {
                iniciarVerificacoes()
            }
        }
    }

    private fun iniciarVerificacoes(){
        if(Dados.recuperarLogin(this)) {
            FirebaseAutenticacao.deslogarUser()
        }

        if(!LocalizacaoPessoa.providerAtivo(this, LocationManager.GPS_PROVIDER)){
            val SnackGps = Snackbar.make(findViewById(R.id.TelaSplash), R.string.GpsDesligado, Snackbar.LENGTH_LONG)
            SnackGps.setAction(R.string.Sim) {
                val Iniciar = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(Iniciar)
            }.show()
        }

        if(VerificarInternet.internetConectado(this) == true){
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
                val Verificado = VerificarInternet.internetConectado(this@TelaSplash)
                if(Verificado == false || Verificado == null) {
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
            BuscarLojasImagem(User, this@TelaSplash)
        } else {
            iniciarActivity(Intent(this@TelaSplash, TelaLogin::class.java))
            finishAffinity()
        }
    }

    private fun iniciarActivity(Iniciar:Intent){
        if(!Iniciado){
            startActivity(Iniciar)
            Iniciado = true
        }
    }

    override fun concluido(Modo: Boolean, Lista: MutableList<Lojas>?, ListaImagem: HashMap<String, Bitmap>?, Pessoa: Perfil?) {
        if(Lista != null && ListaImagem != null){
            ListaLocais.refazer(Lista)
            ListaBitmap.refazer(ListaImagem)
            if(Pessoa != null){
                ListaLocais.refazerFavoritos(Pessoa.lojasFavoritas)
                verificacaoGPS(Pessoa)
            }
        } else {
            prepararInicio(Pessoa)
        }
    }

    private fun verificacaoGPS(Pessoa: Perfil) {
        if(Pessoa.raio != 100L){
            BuscarLojasProximas(this, Pessoa.raio.toDouble() * 1000).procurarProximos(object :
                BuscarLojasProximas.BuscaConcluida {
                override fun resultado(Modo: Boolean) {
                    if(Pausado){
                        PessoaPassada = Pessoa
                    } else {
                        prepararInicio(Pessoa)
                    }
                }
            })
        } else {
            prepararInicio(Pessoa)
        }

    }

    private fun prepararInicio(Pessoa: Perfil?) {
        if (Pessoa?.acesso == Chave.CHAVE_ADM.valor) {
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