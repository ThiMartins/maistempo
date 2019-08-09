package dev.tantto.maistempo.telas

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseUser
import dev.tantto.maistempo.ListaBitmap
import dev.tantto.maistempo.ListaLocais
import dev.tantto.maistempo.adaptadores.AdaptadorPager
import dev.tantto.maistempo.fragmentos.FragmentApresentacao
import dev.tantto.maistempo.fragmentos.FragmentLogin
import dev.tantto.maistempo.fragmentos.FragmentNovoUsuario
import dev.tantto.maistempo.modelos.Lojas
import dev.tantto.maistempo.modelos.Perfil
import dev.tantto.maistempo.R
import dev.tantto.maistempo.chaves.Chave
import dev.tantto.maistempo.classes.BuscarLojasImagem

class TelaLogin : AppCompatActivity(), BuscarLojasImagem.BuscarConcluida {

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
        Pagina?.adapter = AdaptadorPager(Mananger, Lista)
        Pagina?.currentItem = 1
    }

    private fun referenciandoFragments() {
        Login = FragmentLogin().setandoReferencia(this)
        Apresentacao = FragmentApresentacao()
        Novo = FragmentNovoUsuario().setandoReferencia(this)
    }

    fun loginConcluido(User:FirebaseUser?, Pessoa:Perfil? = null){
        if(Pessoa == null && User != null){
            BuscarLojasImagem(User.email!!, this)
        } else if(Pessoa != null){
            BuscarLojasImagem(Pessoa.cidade, Pessoa, this)
        }
    }

    override fun concluido(Modo: Boolean, Lista: MutableList<Lojas>?, ListaImagem: HashMap<String, Bitmap>?, Pessoa: Perfil?) {
        if(Modo && Lista != null && ListaImagem != null && Pessoa != null){
            ListaLocais.refazer(Lista)
            ListaLocais.refazerFavoritos(Pessoa.lojasFavoritas)
            ListaBitmap.refazer(ListaImagem)
            startActivity(Intent(this, TelaPrincipal::class.java))
            finishAffinity()
        }
    }

    override fun onStop() {
        super.onStop()
        if(intent.hasExtra(Chave.CHAVE_FECHAR.valor)){
            finishAffinity()
        }
    }

}
