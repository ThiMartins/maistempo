package dev.tantto.maistempo.Telas

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import dev.tantto.maistempo.Modelos.Lojas
import dev.tantto.maistempo.R

class TelaResumo : AppCompatActivity() {

    private var Endereco:TextView? = null
    private var Telefone:TextView? = null
    private var Status:TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resumo_local)
        ConfigurandoView()

        if(intent.hasExtra("TELAPRINCIPAL")){
            val Item = intent.getSerializableExtra("TELAPRINCIPAL") as Lojas
            Endereco?.text = "Endereco: Nao sei"
            Telefone?.text = "Telefone: 15 991321814"
            Status?.text = "Status: " + Item.Status
            title = Item.Titulo
        }

    }

    private fun ConfigurandoView() {
        Endereco = findViewById<TextView>(R.id.EnderecoResumo)
        Telefone = findViewById<TextView>(R.id.TelefoneResumo)
        Status = findViewById<TextView>(R.id.StatusResumo)
    }

}