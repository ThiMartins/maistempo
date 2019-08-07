package dev.tantto.maistempo.telas

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.tantto.maistempo.R

class TelaAdicionarLoja : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_adicionar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}