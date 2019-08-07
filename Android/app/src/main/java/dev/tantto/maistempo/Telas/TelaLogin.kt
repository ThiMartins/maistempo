package dev.tantto.maistempo.telas

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseUser
import dev.tantto.maistempo.adaptadores.AdaptadorPager
import dev.tantto.maistempo.Fragmentos.FragmentApresentacao
import dev.tantto.maistempo.Fragmentos.FragmentLogin
import dev.tantto.maistempo.Fragmentos.FragmentNovoUsuario
import dev.tantto.maistempo.google.*
import dev.tantto.maistempo.ListaBitmap
import dev.tantto.maistempo.ListaLocais
import dev.tantto.maistempo.Modelos.Lojas
import dev.tantto.maistempo.Modelos.Perfil
import dev.tantto.maistempo.R

class TelaLogin : AppCompatActivity(), DatabaseLocaisInterface, DownloadFotoCloud, DatabasePessoaInterface {

    private var Login:FragmentLogin? = null
    private var Apresentacao:FragmentApresentacao? = null
    private var Novo:FragmentNovoUsuario? = null
    private var Pagina:ViewPager? = null
    private var TabIndicador:TabLayout? = null
    private var Iniciar:Intent? = null
    private var Tamanho = 0
    private var Iniciado = false

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
        Iniciar = Intent(this, TelaPrincipal::class.java)
        Iniciar?.putExtra(Tela.GET_USER, User)
        if(Pessoa != null){
            Iniciar?.putExtra(Tela.GET_PESSOA, Pessoa)
            DatabaseFirebaseRecuperar.recuperarLojasLocais(Pessoa.cidade.toLowerCase(), this)
        } else {
            DatabaseFirebaseRecuperar.recuperaDadosPessoa(User?.email!!, this)
        }
    }

    override fun pessoaRecebida(Pessoa: Perfil) {
        DatabaseFirebaseRecuperar.recuperarLojasLocais(Pessoa.cidade.toLowerCase(), this)
    }

    override fun dadosRecebidosLojas(Lista: MutableList<Lojas>, Erros: String) {
        if(Lista.isNotEmpty()){
            ListaLocais.refazer(Lista)
            Tamanho = Lista.size
            for (Item in Lista){
                CloudStorageFirebase().donwloadCloud("${Item.id}.jpg", TipoDonwload.ICONE, this)
            }
        } else {
            if(Iniciar != null && !Iniciado){
                Iniciado = true
                startActivity(Iniciar)
                finishAffinity()
            }
        }
    }

    override fun imagemBaixada(Imagem: HashMap<String, Bitmap>?) {
        if(Imagem != null){
            ListaBitmap.adicionar(Imagem)
            if(ListaBitmap.tamanho() == Tamanho){
                if(Iniciar != null && !Iniciado){
                    Iniciado = true
                    startActivity(Iniciar)
                    finishAffinity()
                }
            }
        } else {
            if(Iniciar != null && !Iniciado){
                Iniciado = true
                startActivity(Iniciar)
                finishAffinity()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        if(intent.hasExtra("Fechar")){
            finishAffinity()
        }
    }

}
