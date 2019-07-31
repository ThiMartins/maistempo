package dev.tantto.maistempo.Telas

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import dev.tantto.maistempo.Adaptadores.ViewPagerAdaptador
import dev.tantto.maistempo.Chaves.Chaves
import dev.tantto.maistempo.Fragmentos.FragmentAvaliacao
import dev.tantto.maistempo.Fragmentos.FragmentResumo
import dev.tantto.maistempo.Google.DatabaseFirebaseRecuperar
import dev.tantto.maistempo.Google.DatabaseFirebaseSalvar
import dev.tantto.maistempo.Google.FavoritosRecuperados
import dev.tantto.maistempo.Google.FirebaseAutenticacao
import dev.tantto.maistempo.ListaLocais
import dev.tantto.maistempo.Modelos.Lojas
import dev.tantto.maistempo.R

class TelaResumo : AppCompatActivity(), FavoritosRecuperados {

    private var Endereco:TextView? = null
    private var Telefone:TextView? = null
    private var Status:TextView? = null
    private var Pagina:ViewPager? = null
    private var TabIndicator:TabLayout? = null
    private var FilaResumo:FragmentResumo? = null
    private var AvaliacaoResumo:FragmentAvaliacao? = null
    private var Foto:ImageView? = null
    private var LojaInfo:Lojas? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resumo_local)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        configurandoView()
        recuperarLoja()

        Pagina = findViewById<ViewPager>(R.id.PaginaResumo)
        TabIndicator = findViewById<TabLayout>(R.id.TabAvalicao)
        supportActionBar?.elevation = 0.0F
        configurandoPagina()

    }

    private fun recuperarLoja() {
        if (intent.hasExtra(Chaves.CHAVE_TELA_PRINCIPAL.valor)) {
            LojaInfo = intent.getSerializableExtra(Chaves.CHAVE_TELA_PRINCIPAL.valor) as Lojas
            Endereco?.text = String.format(getString(R.string.Endereco)+ " " + LojaInfo?.local)
            Telefone?.text = String.format(getString(R.string.Telefone) + " " + LojaInfo?.telefone)
            Status?.text = String.format(getString(R.string.Status) + " " + LojaInfo?.status?.get(0))
            title = LojaInfo?.titulo

            val Posicao = intent.getIntExtra(Chaves.CHAVE_POSICAO_LISTA.valor, 0)
            //Foto?.setImageBitmap(ListaBitmap.recuperar(Posicao))

        }
    }

    private fun configurandoPagina() {
        TabIndicator?.setupWithViewPager(Pagina)
        FilaResumo = FragmentResumo()
        FilaResumo?.passandoLja(LojaInfo!!, this)
        AvaliacaoResumo = FragmentAvaliacao()
        AvaliacaoResumo?.setandoReferencias(LojaInfo!!, this)
        val ListaFragmentos = listOf(FilaResumo!!, AvaliacaoResumo!!)
        Pagina?.adapter = ViewPagerAdaptador(supportFragmentManager, ListaFragmentos)

        TabIndicator?.getTabAt(0)?.setText(R.string.Fila)
        TabIndicator?.getTabAt(1)?.setText(R.string.Availiacao)
    }

    private fun configurandoView() {
        Endereco = findViewById<TextView>(R.id.EnderecoResumo)
        Telefone = findViewById<TextView>(R.id.TelefoneResumo)
        Status = findViewById<TextView>(R.id.StatusResumo)
        Foto = findViewById<ImageView>(R.id.FotoResumo)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Foto?.clipToOutline = true
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

        return super.onOptionsItemSelected(item)
    }

    override fun recuperado() {

    }

}