package dev.tantto.maistempo.Telas

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.tapadoo.alerter.Alert
import com.tapadoo.alerter.Alerter
import dev.tantto.maistempo.Adaptadores.ViewPagerAdaptador
import dev.tantto.maistempo.Dados.Dados
import dev.tantto.maistempo.Fragmentos.FragmentApresentacao
import dev.tantto.maistempo.Fragmentos.FragmentLogin
import dev.tantto.maistempo.Fragmentos.FragmentNovoUsuario
import dev.tantto.maistempo.Interfaces.EnumTelas
import dev.tantto.maistempo.ModosTela
import dev.tantto.maistempo.R

class TelaLogin : AppCompatActivity() {

    private var Login:FragmentLogin? = null
    private var Apresentacao:FragmentApresentacao? = null
    private var Novo:FragmentNovoUsuario? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_login)

        val Pagina = findViewById<ViewPager>(R.id.VisualizarLogin)
        val TabIndicador = findViewById<TabLayout>(R.id.TabLayout)

        ReferenciandoFragments()
        SetandoTela(TabIndicador, Pagina)

    }

    private fun SetandoTela(TabIndicador: TabLayout, Pagina: ViewPager) {
        val Mananger = supportFragmentManager
        val Lista = listOf(Apresentacao!!, Login!!, Novo!!)

        TabIndicador.setupWithViewPager(Pagina, true)
        Pagina.adapter = ViewPagerAdaptador(Mananger, Lista)
        Pagina.currentItem = 1
    }

    private fun ReferenciandoFragments() {
        if (Login == null && Apresentacao == null) {
            Login = FragmentLogin(this, this)
            Apresentacao = FragmentApresentacao(this)
            Novo = FragmentNovoUsuario(this, this)
        }
    }

    fun LoginConcluido(Pessoa:FirebaseUser?){
        startActivity(Intent(this, TelaPrincipal::class.java))
    }

}
