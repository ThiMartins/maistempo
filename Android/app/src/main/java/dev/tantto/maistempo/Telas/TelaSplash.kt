package dev.tantto.maistempo.Telas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dev.tantto.maistempo.Google.*
import dev.tantto.maistempo.ListaLocais
import dev.tantto.maistempo.Modelos.Lojas
import dev.tantto.maistempo.R
import dev.tantto.maistempo.Servicos.LojasRecuperadas

class TelaSplash : AppCompatActivity(), DatabaseLocaisInterface, LojasRecuperadas {

    private var Recuperado = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_splash)

        //Pedir autorizacao ao usuario
        //Pegar localizacao

    }

    override fun onResume() {
        super.onResume()
        carregandoLogin()
    }

    private fun carregandoLogin() {
        val User = FirebaseAutenticacao.Autenticacao.currentUser?.email
        if (User.isNullOrEmpty()) {
            iniciarActivity(Intent(this, TelaLogin::class.java))
            finishAffinity()
        } else {
            DatabaseFirebaseRecuperar.RecuperarLocal("Sorocaba", this)
        }
    }

    override fun DadosRecebidosImagens(Lista: MutableList<Lojas>) {

    }

    override fun DadosRecebidos(Lista: MutableList<Lojas>) {
        ListaLocais.Refazer(Lista)
        iniciarActivity(Intent(this, TelaPrincipal::class.java))
        finishAffinity()
    }

    private fun iniciarActivity(Iniciar:Intent){
        startActivity(Iniciar)
    }
}