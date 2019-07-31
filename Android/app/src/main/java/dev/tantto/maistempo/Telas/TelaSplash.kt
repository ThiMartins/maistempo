package dev.tantto.maistempo.Telas

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.firebase.functions.FirebaseFunctions
import dev.tantto.maistempo.Classes.Alertas
import dev.tantto.maistempo.Classes.Permissao
import dev.tantto.maistempo.Classes.Permissoes
import dev.tantto.maistempo.Classes.TipoDePermissao
import dev.tantto.maistempo.Google.*
import dev.tantto.maistempo.ListaBitmap
import dev.tantto.maistempo.ListaLocais
import dev.tantto.maistempo.Modelos.Lojas
import dev.tantto.maistempo.Modelos.Perfil
import dev.tantto.maistempo.R

class TelaSplash : AppCompatActivity(), DatabaseLocaisInterface, DownloadFotoCloud, FavoritosRecuperados, DatabasePessoaInterface {

    private val RequisicaoPermissaoCamera = 0
    private val RequisicaoPermissaoLeitura = 1
    private val RequisicaoPermissaoEscrita = 2
    private val RequisicaoPermissaoFine = 3
    private val RequisicaoPermissaoCoarse = 4
    private var Iniciado = false
    private var Tamanho = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_splash)
        supportActionBar?.elevation = 0F

        /*val Valores = hashMapOf(Pair("id", "7KcJkXNU8Pn7n4UxYD6B"), Pair("valor", "123"))

        FirebaseFunctions.getInstance().getHttpsCallable("adicionarFila").call(Valores).addOnCompleteListener {
            Log.i("Teste", it.exception?.localizedMessage)
        }*/
    }

    override fun onResume() {
        super.onResume()

        //Pedir as outras permissoes

        when {
            Permissao.veficarPermissao(this, Permissoes.CAMERA) != TipoDePermissao.PERMITIDO -> ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), RequisicaoPermissaoCamera)
            Permissao.veficarPermissao(this, Permissoes.ARMAZENAMENTO_READ) != TipoDePermissao.PERMITIDO -> ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), RequisicaoPermissaoLeitura)
            Permissao.veficarPermissao(this, Permissoes.ARMAZENAMENTO_WRITE) != TipoDePermissao.PERMITIDO -> ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), RequisicaoPermissaoEscrita)
            Permissao.veficarPermissao(this, Permissoes.LOCALIZACAO_COARSE) != TipoDePermissao.PERMITIDO -> ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), RequisicaoPermissaoCoarse)
            Permissao.veficarPermissao(this, Permissoes.LOCALIZACAO_FINE) != TipoDePermissao.PERMITIDO -> ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), RequisicaoPermissaoFine)
            else -> carregandoLogin()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //carregandoLogin()
    }

    override fun dadosRecebidos(Lista: MutableList<Lojas>) {
        ListaLocais.refazer(Lista)
        Tamanho = Lista.size
        if(!FirebaseAutenticacao.Autenticacao.currentUser?.email.isNullOrEmpty()){
            DatabaseFirebaseRecuperar.recuperarFavoritos(FirebaseAutenticacao.Autenticacao.currentUser?.email!!, this)
        }

        if(Lista.isNotEmpty()){
            for (Item in Lista){
                CloudStorageFirebase().donwloadCloud(Item.id, TipoDonwload.ICONE, this)
            }
        } else {
            iniciarActivity(Intent(this, TelaPrincipal::class.java))
            finishAffinity()
        }
    }

    override fun recuperado() {

    }

    override fun imagemBaixada(Imagem: Bitmap?) {
        //ListaBitmap.adicionar(Imagem)
        //if(ListaBitmap.tamanho() == Tamanho){
            if(!FirebaseAutenticacao.Autenticacao.currentUser?.email.isNullOrEmpty() && !Iniciado){
                iniciarActivity(Intent(this, TelaPrincipal::class.java))
                finishAffinity()
            }
        //}
    }


    private fun carregandoLogin() {
        val User = FirebaseAutenticacao.Autenticacao.currentUser?.email

        if (!User.isNullOrEmpty()) {
            DatabaseFirebaseRecuperar.recuperaDadosPessoa(User, this)
        } else {
            iniciarActivity(Intent(this, TelaLogin::class.java))
            finishAffinity()
        }
    }

    override fun pessoaRecebida(Pessoa: Perfil) {
        if (Pessoa.email.isNotEmpty()) {
            DatabaseFirebaseRecuperar.recuperaDadosPessoa(Pessoa.email, this)
            DatabaseFirebaseRecuperar.recuperarLojasLocais(Pessoa.cidade, this)
        }
    }

    private fun iniciarActivity(Iniciar:Intent){
        Iniciado = true
        startActivity(Iniciar)
    }
}