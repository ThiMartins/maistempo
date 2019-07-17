package dev.tantto.maistempo.Telas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabItem
import com.google.android.material.tabs.TabLayout
import dev.tantto.maistempo.Adaptadores.ViewPagerAdaptador
import dev.tantto.maistempo.Fragmentos.FragmentLocal
import dev.tantto.maistempo.Fragmentos.FragmentPerfil
import dev.tantto.maistempo.ListaLocais
import dev.tantto.maistempo.ListaPerfil
import dev.tantto.maistempo.Modelos.Lojas
import dev.tantto.maistempo.R

class TelaPrincipal : AppCompatActivity() {

    private var TodosLocais:FragmentLocal? = null
    private var FavoritosLocais:FragmentLocal? = null
    private var Perfil:FragmentPerfil? = null
    private var Tabs:TabLayout? = null
    private var Pagina:ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_principal)

        Tabs = findViewById<TabLayout>(R.id.TabPrincipal)
        Pagina = findViewById<ViewPager>(R.id.PagerPrincipal)

        SetantoFragmentos()
        ConfigurandoPager()
        SetandoTabItens()

    }

    private fun SetantoFragmentos() {
        TodosLocais = FragmentLocal(this, ListaLocais().RecuperarTudo())
        FavoritosLocais = FragmentLocal(this, ListaLocais().RecuperarTudo().asReversed())
        Perfil = FragmentPerfil(this, ListaPerfil().RecuperarTudo())
    }

    private fun ConfigurandoPager() {
        Tabs?.setupWithViewPager(Pagina)
        val ListaFragmentos = listOf(TodosLocais!!, FavoritosLocais!!, Perfil!!)
        Pagina?.adapter = ViewPagerAdaptador(supportFragmentManager, ListaFragmentos)
    }

    private fun SetandoTabItens() {
        Tabs?.getTabAt(0)?.setIcon(R.drawable.restaurant_black)?.setText(R.string.Locais)
        Tabs?.getTabAt(1)?.setIcon(R.drawable.star_black)?.setText(R.string.Favoritos)
        Tabs?.getTabAt(2)?.setIcon(R.drawable.setting_dark)?.setText(R.string.Configuracoes)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_pesquisa, menu)

        val PesquisaItem = menu?.findItem(R.id.PesquisaLocal)
        val Pesquisa = PesquisaItem?.actionView as SearchView
        Pesquisa.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String?): Boolean {
                if(Tabs?.selectedTabPosition == 0){
                    TodosLocais?.Filtro(newText!!)
                } else if(Tabs?.selectedTabPosition == 1){
                    FavoritosLocais?.Filtro(newText!!)
                }
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

}
