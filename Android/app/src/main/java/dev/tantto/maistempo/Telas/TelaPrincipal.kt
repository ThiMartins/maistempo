package dev.tantto.maistempo.Telas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import dev.tantto.maistempo.Adaptadores.ViewPagerAdaptador
import dev.tantto.maistempo.Fragmentos.FragmentLocal
import dev.tantto.maistempo.Fragmentos.FragmentPerfil
import dev.tantto.maistempo.R

class TelaPrincipal : AppCompatActivity(){

    private var TodosLocais:FragmentLocal = FragmentLocal()
    private var FavoritosLocais:FragmentLocal = FragmentLocal()
    private var Perfil:FragmentPerfil = FragmentPerfil()
    private var Tabs:TabLayout? = null
    private var Pagina:ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_principal)

        Tabs = findViewById<TabLayout>(R.id.TabPrincipal)
        Pagina = findViewById<ViewPager>(R.id.PagerPrincipal)

        configurandoPager()
        setandoTabItens()

    }

    private fun configurandoPager() {
        Tabs?.setupWithViewPager(Pagina)
        val ListaFragmentos = listOf(TodosLocais, FavoritosLocais, Perfil)
        Pagina?.adapter = ViewPagerAdaptador(supportFragmentManager, ListaFragmentos)
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

        if(Id == R.id.AbrirMapa){
            startActivity(Intent(this, TelaMapa::class.java))
        }

        return super.onOptionsItemSelected(item)
    }

}