package dev.tantto.maistempo.Telas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dev.tantto.maistempo.Dados.Dados
import dev.tantto.maistempo.Google.DatabaseFirebaseRecuperar
import dev.tantto.maistempo.Google.DatabaseLocaisInterface
import dev.tantto.maistempo.Google.FirebaseAutenticacao
import dev.tantto.maistempo.ListaLocais
import dev.tantto.maistempo.Modelos.Lojas
import dev.tantto.maistempo.R

class TelaSplash : AppCompatActivity(), DatabaseLocaisInterface {

    private var logar = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_splash)

        //Pedir autorizacao ao usuario

    }

    override fun onResume() {
        super.onResume()
        carregandoLogin()
    }

    private fun carregandoLogin() {
        val User = FirebaseAutenticacao.Autenticacao.currentUser?.email
        if (User.isNullOrEmpty()) {
            logar = true
            iniciarActivity(Intent(this, TelaLogin::class.java))
        } else {
            DatabaseFirebaseRecuperar.RecuperarLocal(Dados(this).Local(), this)
        }
    }

    override fun onRestart() {
        super.onRestart()
        finishAffinity()
    }

    override fun DadosRecebidos(Lista: MutableList<Lojas>) {
        ListaLocais.Refazer(Lista)
        iniciarActivity(Intent(this, TelaPrincipal::class.java))
    }

    private fun iniciarActivity(Iniciar:Intent){
        startActivity(Iniciar)
    }

}