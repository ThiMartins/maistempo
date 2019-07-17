package dev.tantto.maistempo.Telas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseUser
import dev.tantto.maistempo.Adaptadores.ViewPagerAdaptador
import dev.tantto.maistempo.Fragmentos.FragmentApresentacao
import dev.tantto.maistempo.Fragmentos.FragmentLogin
import dev.tantto.maistempo.Fragmentos.FragmentNovoUsuario
import dev.tantto.maistempo.R

class TelaLogin : AppCompatActivity() {

    private var Login:FragmentLogin? = null
    private var Apresentacao:FragmentApresentacao? = null
    private var Novo:FragmentNovoUsuario? = null
    private var Pagina:ViewPager? = null
    private var TabIndicador:TabLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_login)

        ConfiguracaoView()
        ReferenciandoFragments()
        SetandoTela()

    }

    fun MudarTela(Valor:Int) {
        Pagina?.currentItem = Valor
    }

    private fun ConfiguracaoView() {
        Pagina = findViewById<ViewPager>(R.id.VisualizarLogin)
        TabIndicador = findViewById<TabLayout>(R.id.TabLayout)
    }

    private fun SetandoTela() {
        val Mananger = supportFragmentManager
        val Lista = listOf(Apresentacao!!, Login!!, Novo!!)

        TabIndicador?.setupWithViewPager(Pagina, true)
        Pagina?.adapter = ViewPagerAdaptador(Mananger, Lista)
        Pagina?.currentItem = 1
    }

    private fun ReferenciandoFragments() {
        if (Login == null && Apresentacao == null) {
            Login = FragmentLogin(this, this)
            Apresentacao = FragmentApresentacao(this)
            Novo = FragmentNovoUsuario(this, this)
        }
    }

    fun LoginConcluido(Pessoa:FirebaseUser?){
        val Iniciar = Intent(this, TelaPrincipal::class.java)
        Iniciar.putExtra(Telas.GET_USER, Pessoa)
        startActivity(Iniciar)
    }

}
