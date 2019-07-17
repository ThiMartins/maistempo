package dev.tantto.maistempo.Telas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dev.tantto.maistempo.Dados.Dados
import dev.tantto.maistempo.R

class TelaSplash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_splash)
    }

    override fun onResume() {
        super.onResume()

        if(!Dados(this).Logado()){
            val Logar = Intent(this, TelaLogin::class.java)
            startActivity(Logar)
        } else {
            val Iniciar = Intent(this, TelaPrincipal::class.java)
            startActivity(Iniciar)
        }
    }

    override fun onRestart() {
        super.onRestart()
        finishAffinity()
    }
}
