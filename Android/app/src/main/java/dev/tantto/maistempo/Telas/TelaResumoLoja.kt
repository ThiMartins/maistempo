package dev.tantto.maistempo.telas

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import dev.tantto.maistempo.adaptadores.AdaptadorPager
import dev.tantto.maistempo.chaves.Chave
import dev.tantto.maistempo.fragmentos.FragmentAvaliacao
import dev.tantto.maistempo.fragmentos.FragmentResumo
import dev.tantto.maistempo.ListaBitmap
import dev.tantto.maistempo.ListaLocais
import dev.tantto.maistempo.modelos.Lojas
import dev.tantto.maistempo.R
import dev.tantto.maistempo.google.*
import java.util.*

class TelaResumoLoja : AppCompatActivity(), FavoritosRecuperados, LojaRecuperada {

    private var Endereco:TextView? = null
    private var Telefone:TextView? = null
    private var Status:TextView? = null
    private var Pagina:ViewPager? = null
    private var TabIndicator:TabLayout? = null
    private var FilaResumo:FragmentResumo = FragmentResumo()
    private var AvaliacaoResumo:FragmentAvaliacao = FragmentAvaliacao()
    private var Foto:ImageView? = null
    private var LojaInfo:Lojas? = null
    private var Swipe:SwipeRefreshLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resumo_local)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        configurandoView()
        recuperarLoja()

        Pagina = findViewById(R.id.PaginaResumo)
        TabIndicator = findViewById(R.id.TabAvalicao)
        supportActionBar?.elevation = 0.0F
        configurandoPagina()

    }

    override fun onRestart() {
        super.onRestart()
        FilaResumo.atualizar()
    }

    private fun recuperarLoja() {
        if (intent.hasExtra(Chave.CHAVE_TELA_PRINCIPAL.valor)) {
            LojaInfo = intent.getSerializableExtra(Chave.CHAVE_TELA_PRINCIPAL.valor) as Lojas
            Endereco?.text = String.format(getString(R.string.Endereco)+ " " + LojaInfo?.local)
            Telefone?.text = String.format(getString(R.string.Telefone) + " " + LojaInfo?.telefone)

            title = LojaInfo?.titulo

            if(ListaBitmap.tamanho() > 0){
                Foto?.setImageBitmap(ListaBitmap.recuperar(LojaInfo?.id!!))
            }


            val Horas = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

            if(Horas >= LojaInfo?.horarioInicio!! && Horas <= LojaInfo?.horarioFinal!!){
                Status?.text = getString(R.string.Aberto)
            } else {
                Status?.text = getString(R.string.Fechado)
            }

        }
    }

    fun recuperarLoja(Id:String){
        DatabaseFirebaseRecuperar.recuperarDadosLoja(Id, this)
    }

    private fun configurandoPagina() {
        TabIndicator?.setupWithViewPager(Pagina)
        FilaResumo.passandoLja(LojaInfo!!, this)
        AvaliacaoResumo.setandoReferencias(LojaInfo!!, this)
        val ListaFragmentos = listOf(FilaResumo, AvaliacaoResumo)
        Pagina?.adapter = AdaptadorPager(supportFragmentManager, ListaFragmentos)

        TabIndicator?.getTabAt(0)?.setText(R.string.Fila)
        TabIndicator?.getTabAt(1)?.setText(R.string.Availiacao)
    }

    private fun configurandoView() {
        Endereco = findViewById(R.id.EnderecoResumo)
        Telefone = findViewById(R.id.TelefoneResumo)
        Status = findViewById(R.id.StatusResumo)
        Foto = findViewById(R.id.FotoResumo)
        Swipe = findViewById(R.id.SwipeResumo)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Foto?.clipToOutline = true
        }

        Swipe?.setOnRefreshListener {
            recuperarLoja(LojaInfo?.id!!)
        }
    }

    override fun dados(Loja: Lojas?) {
        if(Loja != null){
            ListaLocais.editar(Loja)
            LojaInfo = Loja
            FilaResumo.atualizarLoja(Loja)
            AvaliacaoResumo.atualizarLoja(Loja)
            Swipe?.isRefreshing = false
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_favoritar, menu)
        if(ListaLocais.contemFavoritos(LojaInfo?.id)){
            menu?.findItem(R.id.Favoritar)?.setIcon(R.drawable.star_full_white)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val Id = item?.itemId

        if(Id == R.id.Favoritar){
            if(ListaLocais.contemFavoritos(LojaInfo?.id)){
                item.setIcon(R.drawable.star_clear)
                DatabaseFirebaseSalvar.removerFavorito(FirebaseAutenticacao.Autenticacao.currentUser?.email.toString(), LojaInfo?.id!!)
                ListaLocais.removerFavoritos(LojaInfo?.id!!)
            } else {
                item.setIcon(R.drawable.star_full_white)
                DatabaseFirebaseSalvar.adicionarFavorito(FirebaseAutenticacao.Autenticacao.currentUser?.email.toString(), LojaInfo?.id!!)
                DatabaseFirebaseRecuperar.recuperarFavoritos(FirebaseAutenticacao.Autenticacao.currentUser?.email.toString(), this)
                ListaLocais.adicionarFavorito(LojaInfo?.id!!)
            }

        }

        return super.onOptionsItemSelected(item!!)
    }

    override fun recuperadoFavoritos() {

    }

}