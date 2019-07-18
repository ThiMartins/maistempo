package dev.tantto.maistempo.telas

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import dev.tantto.maistempo.Adaptadores.ViewPagerAdaptador
import dev.tantto.maistempo.Fragmentos.FragmentAvaliacao
import dev.tantto.maistempo.Fragmentos.FragmentResumo
import dev.tantto.maistempo.Modelos.Lojas
import dev.tantto.maistempo.R

class TelaResumo : AppCompatActivity() {

    private var Endereco:TextView? = null
    private var Telefone:TextView? = null
    private var Status:TextView? = null
    private var Pagina:ViewPager? = null
    private var TabIndicator:TabLayout? = null
    private var FilaResumo:FragmentResumo? = null
    private var AvaliacaoResumo:FragmentAvaliacao? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resumo_local)
        ConfigurandoView()

        if(intent.hasExtra("TELAPRINCIPAL")){
            val Item = intent.getSerializableExtra("TELAPRINCIPAL") as Lojas
            Endereco?.text = "Endereco: " + Item.Local
            Telefone?.text = "Telefone: 15 991321814"
            Status?.text = "Status: " + Item.status
            title = Item.titulo
        }

        Pagina = findViewById<ViewPager>(R.id.PaginaResumo)
        TabIndicator = findViewById<TabLayout>(R.id.TabAvalicao)



        TabIndicator?.setupWithViewPager(Pagina)
        FilaResumo = FragmentResumo(this)
        AvaliacaoResumo = FragmentAvaliacao(this)
        val ListaFragmentos = listOf(FilaResumo!!, AvaliacaoResumo!!)
        Pagina?.adapter = ViewPagerAdaptador(supportFragmentManager, ListaFragmentos)

        TabIndicator?.getTabAt(0)?.setText(R.string.Fila)
        TabIndicator?.getTabAt(1)?.setText(R.string.Availiacao)

    }

    private fun ConfigurandoView() {
        Endereco = findViewById<TextView>(R.id.EnderecoResumo)
        Telefone = findViewById<TextView>(R.id.TelefoneResumo)
        Status = findViewById<TextView>(R.id.StatusResumo)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_favoritar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val Id = item?.itemId

        if(Id == R.id.Favoritar){
            item.setIcon(R.drawable.star_full_white)
        }

        return super.onOptionsItemSelected(item)
    }

}