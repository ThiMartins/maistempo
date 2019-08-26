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
import dev.tantto.maistempo.classes.Alertas
import dev.tantto.maistempo.classes.BuscarLojasImagem
import dev.tantto.maistempo.google.CidadesRecuperadas
import dev.tantto.maistempo.google.DatabaseFirebaseRecuperar

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
        buscarCidade()
    }

    fun mudarTela(Valor:Int) {
        Pagina?.currentItem = Valor
    }

    private fun configuracaoView() {
        Pagina = findViewById(R.id.VisualizarLogin)
        TabIndicador = findViewById(R.id.TabLayout)
    }

    private fun setandoTela() {
        val Mananger = supportFragmentManager
        val Lista = listOf(Apresentacao!!, Login!!, Novo!!)

        TabIndicador?.setupWithViewPager(Pagina, true)
        Pagina?.adapter = AdaptadorPager(Mananger, Lista)
        Pagina?.currentItem = 1

        TabIndicador?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
                if (p0?.position == 2){
                    Novo?.limparConteudo()
                } else if (p0?.position == 1){
                    Login?.limparConteudo()
                }
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {

            }
        })
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

    private fun buscarCidade(){
        val carregar = Alertas.alertaCarregando(this)
        carregar.show()

        DatabaseFirebaseRecuperar.recuperarCidades(object : CidadesRecuperadas {
            override fun listaCidades(Lista: List<String>?) {
                carregar.dismiss()
                if(Lista != null){
                    Novo?.passandoCidades(Lista)
                }
            }
        })
    }

    override fun concluido(Modo: Boolean, Lista: MutableList<Lojas>?, ListaImagem: HashMap<String, Bitmap>?, Pessoa: Perfil?) {
        if(Modo && Lista != null && ListaImagem != null && Pessoa != null){
            val Iniciar = Intent(this, TelaPrincipal::class.java)
            if(Pessoa.acesso == Chave.CHAVE_ADM.valor){
                Iniciar.putExtra(Chave.CHAVE_ACESSO.valor, Chave.CHAVE_ADM.valor)
            }
            ListaLocais.refazer(Lista)
            ListaLocais.refazerFavoritos(Pessoa.lojasFavoritas)
            ListaBitmap.refazer(ListaImagem)
            startActivity(Iniciar)
            finishAffinity()
        }
    }

    override fun onStop() {
        super.onStop()
        if(intent.hasExtra(Chave.CHAVE_FECHAR.valor)){
            finishAffinity()
        }
        Novo?.limparConteudo()
        Login?.limparConteudo()
    }

}
