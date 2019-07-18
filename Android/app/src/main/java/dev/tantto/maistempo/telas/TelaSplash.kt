package dev.tantto.maistempo.telas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dev.tantto.maistempo.Dados.Dados
import dev.tantto.maistempo.Google.GoogleFirebaseRealtimeDatabase
import dev.tantto.maistempo.Google.Recuperados
import dev.tantto.maistempo.ListaLocais
import dev.tantto.maistempo.Modelos.Lojas
import dev.tantto.maistempo.R

class TelaSplash : AppCompatActivity(), Recuperados {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_splash)
    }

    override fun onResume() {
        super.onResume()
        GoogleFirebaseRealtimeDatabase.RecuperarDadosLocal(0.0, 1000.0, this)
    }

    override fun onRestart() {
        super.onRestart()
        finishAffinity()
    }

    override fun Recuperado(Lista: List<Lojas>) {
        ListaLocais.Refazer(Lista)
        if(!Dados(this).Logado()){
            val Logar = Intent(this, TelaLogin::class.java)
            startActivity(Logar)
        } else {
            val Iniciar = Intent(this, TelaPrincipal::class.java)
            startActivity(Iniciar)
        }
    }

}