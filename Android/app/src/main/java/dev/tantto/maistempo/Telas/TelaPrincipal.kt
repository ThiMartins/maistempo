package dev.tantto.maistempo.Telas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import dev.tantto.maistempo.Adaptadores.ViewPagerAdaptador
import dev.tantto.maistempo.Classes.Alertas
import dev.tantto.maistempo.Fragmentos.FragmentFavoritos
import dev.tantto.maistempo.Fragmentos.FragmentLocal
import dev.tantto.maistempo.Fragmentos.FragmentPerfil
import dev.tantto.maistempo.Google.*
import dev.tantto.maistempo.ListaLocais
import dev.tantto.maistempo.Modelos.Perfil
import dev.tantto.maistempo.R

class TelaPrincipal : AppCompatActivity(), DatabasePessoaInterface, FavoritosRecuperados{

    private var TodosLocais:FragmentLocal = FragmentLocal()
    private var FavoritosLocais:FragmentFavoritos = FragmentFavoritos()
    private var Perfil:FragmentPerfil = FragmentPerfil()
    private var Tabs:TabLayout? = null
    private var Pagina:ViewPager? = null
    private var Pessoa:Perfil? = null
    private var ProgressoRaio:SeekBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_principal)

        Tabs = findViewById<TabLayout>(R.id.TabPrincipal)
        Pagina = findViewById<ViewPager>(R.id.PagerPrincipal)

        configurandoPager()
        setandoTabItens()

        DatabaseFirebaseRecuperar.recuperaDadosPessoa(FirebaseAutenticacao.Autenticacao.currentUser?.email!!, this)
    }

    private fun configurandoPager() {
        Tabs?.setupWithViewPager(Pagina)
        val ListaFragmentos = listOf(TodosLocais, FavoritosLocais, Perfil)
        Pagina?.adapter = ViewPagerAdaptador(supportFragmentManager, ListaFragmentos)

    }

    override fun onRestart() {
        super.onRestart()
        //DatabaseFirebaseRecuperar.recuperarFavoritos(FirebaseAutenticacao.Autenticacao.currentUser?.email!!, this)
        FavoritosLocais.reloadLista()
    }

    override fun onResume() {
        super.onResume()
        if(ListaLocais.tamanho() == 0){
            Alertas.criarAlerter(this, R.string.ErroCidade, R.string.Atencao).show()
        }
    }

    override fun recuperadoFavoritos() {
        FavoritosLocais.reloadLista()
    }

    private fun setandoTabItens() {
        Tabs?.getTabAt(0)?.setIcon(R.drawable.restaurant_black)?.setText(R.string.Locais)
        Tabs?.getTabAt(1)?.setIcon(R.drawable.star_black)?.setText(R.string.Favoritos)
        Tabs?.getTabAt(2)?.setIcon(R.drawable.setting_dark)?.setText(R.string.Configuracoes)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_tela_principal, menu)

        val PesquisaItem = menu?.findItem(R.id.PesquisaLocal)
        val Pesquisa = PesquisaItem?.actionView as SearchView
        Pesquisa.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String?): Boolean {
                if(Tabs?.selectedTabPosition == 0){
                    TodosLocais.filtro(newText!!)
                } else if(Tabs?.selectedTabPosition == 1){
                    FavoritosLocais.filtro(newText!!)
                }
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val Id = item?.itemId

        when(Id){
            R.id.AbrirMapa -> startActivity(Intent(this, TelaMapa::class.java))
            R.id.MudarRaio -> mudarRaio()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun mudarRaio(){
        val Alerta = AlertDialog.Builder(this)
        Alerta
            .setView(R.layout.mudar_raio)
            .setTitle(R.string.MudarRaio)
            .setPositiveButton(R.string.Enviar){ _, _ ->
                if(ProgressoRaio?.progress != Pessoa?.raio?.toInt()){
                    DatabaseFirebaseSalvar.mudarRaio(Pessoa?.email!!, ProgressoRaio?.progress!!)
                }
            }.setNegativeButton(R.string.Cancelar, null)
        val AlertaFechado = Alerta.create()
        AlertaFechado.show()
        ProgressoRaio = AlertaFechado.findViewById<SeekBar>(R.id.ValorMudarRaio)
        ProgressoRaio?.progress = Pessoa?.raio?.toInt()!!
    }

    override fun pessoaRecebida(Pessoa: Perfil) {
        this.Pessoa = Pessoa
    }

}