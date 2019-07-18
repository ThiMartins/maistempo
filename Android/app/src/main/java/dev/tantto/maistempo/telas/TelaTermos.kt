package dev.tantto.maistempo.telas

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.CheckBox
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import dev.tantto.maistempo.R

class TelaTermos : AppCompatActivity() {

    private var Checado:CheckBox? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_termo_de_uso)
        Checado = findViewById(R.id.TermoCheked)
        title = getString(R.string.TermosDeUso)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_done, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val Id = item?.itemId

        if (Id == R.id.DoneTermos){
            if(Checado?.isChecked!!){
                this.finish()
            } else {
                val Alerta = AlertDialog.Builder(this)
                Alerta.setTitle(R.string.Atencao)
                Alerta.setMessage(R.string.MensagemTermos)
                //SetOnClik // o _ é para se nao precisa dar variaveis
                Alerta.setPositiveButton(R.string.Concordo) { _, _ ->
                    startActivity(Intent(this, TelaLogin::class.java))
                    this.finishAffinity()
                }
                Alerta.setNegativeButton(R.string.Discordo, null)
                Alerta.show()
            }
        }

        return super.onOptionsItemSelected(item)
    }

}