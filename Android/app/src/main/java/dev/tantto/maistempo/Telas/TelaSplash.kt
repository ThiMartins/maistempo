package dev.tantto.maistempo.Telas

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.firebase.functions.FirebaseFunctions
import dev.tantto.maistempo.Classes.Alertas
import dev.tantto.maistempo.Classes.Permissao
import dev.tantto.maistempo.Classes.Permissoes
import dev.tantto.maistempo.Classes.TipoDePermissao
import dev.tantto.maistempo.Google.*
import dev.tantto.maistempo.ListaLocais
import dev.tantto.maistempo.Modelos.Lojas
import dev.tantto.maistempo.R
import dev.tantto.maistempo.Servicos.LojasRecuperadas

class TelaSplash : AppCompatActivity(), DatabaseLocaisInterface, LojasRecuperadas {

    private val RequisicaoPermissao = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_splash)
        //Pegar localizacao
    }

    override fun onResume() {
        super.onResume()

        FirebaseFunctions.getInstance().getHttpsCallable("mudarValor").call()
        DatabaseFirebaseSalvar.BancoFirestore.collection("usuarios").document("email@teste.com").update("pontosLocais", 7).addOnSuccessListener {
            Toast.makeText(this, "Enviado", Toast.LENGTH_SHORT).show()
        }

        if(Permissao.veficarPermissao(this, Permissoes.CAMERA) != TipoDePermissao.PERMITIDO){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), RequisicaoPermissao)
        } else {
            carregandoLogin()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            RequisicaoPermissao -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Alertas.CriarTela(this, R.string.Permissao, R.string.Atencao, 5000)
                } else{
                    Alertas.CriarTela(this, R.string.ErroPermissaoFoto, R.string.Atencao, 5000)
                }
                carregandoLogin()
            }
        }
    }

    private fun carregandoLogin() {
        val User = FirebaseAutenticacao.Autenticacao.currentUser?.email
        if (User.isNullOrEmpty()) {
            DatabaseFirebaseRecuperar.recuperarLojasLocais("Sorocaba", this)
            iniciarActivity(Intent(this, TelaLogin::class.java))
            finishAffinity()
        } else {
            DatabaseFirebaseRecuperar.recuperarLojasLocais("Sorocaba", this)
        }
    }

    override fun DadosRecebidosImagens(Lista: MutableList<Lojas>) {

    }

    override fun dadosRecebidos(Lista: MutableList<Lojas>) {
        ListaLocais.Refazer(Lista)
        if(!FirebaseAutenticacao.Autenticacao.currentUser?.email.isNullOrEmpty()){
            iniciarActivity(Intent(this, TelaPrincipal::class.java))
            Log.i("Teste", "Passou")
        }
        finishAffinity()
    }

    private fun iniciarActivity(Iniciar:Intent){
        startActivity(Iniciar)
    }
}