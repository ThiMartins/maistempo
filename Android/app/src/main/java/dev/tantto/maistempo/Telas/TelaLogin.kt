package dev.tantto.maistempo.Telas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseUser
import dev.tantto.maistempo.Adaptadores.ViewPagerAdaptador
import dev.tantto.maistempo.Fragmentos.FragmentApresentacao
import dev.tantto.maistempo.Fragmentos.FragmentLogin
import dev.tantto.maistempo.Fragmentos.FragmentNovoUsuario
import dev.tantto.maistempo.Modelos.Perfil
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

        configuracaoView()
        referenciandoFragments()
        setandoTela()

    }

    fun mudarTela(Valor:Int) {
        Pagina?.currentItem = Valor
    }

    private fun configuracaoView() {
        Pagina = findViewById<ViewPager>(R.id.VisualizarLogin)
        TabIndicador = findViewById<TabLayout>(R.id.TabLayout)
    }

    private fun setandoTela() {
        val Mananger = supportFragmentManager
        val Lista = listOf(Apresentacao!!, Login!!, Novo!!)

        TabIndicador?.setupWithViewPager(Pagina, true)
        Pagina?.adapter = ViewPagerAdaptador(Mananger, Lista)
        Pagina?.currentItem = 1
    }

    private fun referenciandoFragments() {
        Login = FragmentLogin().setandoReferencia(this)
        Apresentacao = FragmentApresentacao()
        Novo = FragmentNovoUsuario().setandoReferencia(this, this)
    }

    fun loginConcluido(User:FirebaseUser?, Pessoa:Perfil? = null){
        val Iniciar = Intent(this, TelaPrincipal::class.java)
        Iniciar.putExtra(Telas.GET_USER, User)
        if(Pessoa != null){
            Iniciar.putExtra(Telas.GET_PESSOA, Pessoa)
        }
        startActivity(Iniciar)
        finishAffinity()
    }
}
