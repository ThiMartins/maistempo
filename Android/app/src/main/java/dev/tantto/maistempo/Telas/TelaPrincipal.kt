package dev.tantto.maistempo.telas

import android.content.Intent
import android.graphics.Bitmap
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import dev.tantto.maistempo.ListaBitmap
import dev.tantto.maistempo.ListaLocais
import dev.tantto.maistempo.classes.*
import dev.tantto.maistempo.adaptadores.AdaptadorPager
import dev.tantto.maistempo.fragmentos.FragmentFavoritos
import dev.tantto.maistempo.fragmentos.FragmentLocal
import dev.tantto.maistempo.fragmentos.FragmentPerfil
import dev.tantto.maistempo.google.*
import dev.tantto.maistempo.modelos.Perfil
import dev.tantto.maistempo.R
import dev.tantto.maistempo.chaves.Chave
import dev.tantto.maistempo.modelos.Lojas

class TelaPrincipal : AppCompatActivity(), FavoritosRecuperados{

    private var TodosLocais:FragmentLocal = FragmentLocal()
    private var FavoritosLocais:FragmentFavoritos = FragmentFavoritos()
    private var Perfil:FragmentPerfil = FragmentPerfil()
    private var Tabs:TabLayout? = null
    private var Pagina:ViewPager? = null
    private var ProgressoRaio:SeekBar? = null
    private var NivelAcesso:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_principal)

        Tabs = findViewById<TabLayout>(R.id.TabPrincipal)
        Pagina = findViewById<ViewPager>(R.id.PagerPrincipal)

        configurandoPager()
        setandoTabItens()
        verificarGps()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(Chave.CHAVE_ACESSO.valor, NivelAcesso)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        TodosLocais.passandoReferencia(this)
        if(savedInstanceState.containsKey(Chave.CHAVE_ACESSO.valor)){
            NivelAcesso = savedInstanceState.getString(Chave.CHAVE_ACESSO.valor, "")
        }
    }

    override fun onRestart() {
        super.onRestart()
        FavoritosLocais.reloadLista()
    }

    override fun recuperadoFavoritos() {
        FavoritosLocais.reloadLista()
    }

    private fun configurandoPager() {
        TodosLocais.passandoReferencia(this)
        Tabs?.setupWithViewPager(Pagina)
        val ListaFragmentos = listOf(TodosLocais, FavoritosLocais, Perfil)
        Pagina?.adapter = AdaptadorPager(supportFragmentManager, ListaFragmentos)
    }

    private fun setandoTabItens() {
        Tabs?.getTabAt(0)?.setIcon(R.drawable.shopping_black)?.setText(R.string.Locais)
        Tabs?.getTabAt(1)?.setIcon(R.drawable.star_black)?.setText(R.string.Favoritos)
        Tabs?.getTabAt(2)?.setIcon(R.drawable.setting_dark)?.setText(R.string.Configuracoes)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(intent.hasExtra(Chave.CHAVE_ACESSO.valor)){
            NivelAcesso = Chave.CHAVE_ADM.valor
            menuInflater.inflate(R.menu.menu_tela_principal_adm, menu)
        } else {
            menuInflater.inflate(R.menu.menu_tela_principal, menu)
        }

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

        when(item?.itemId){
            R.id.AbrirMapa -> startActivity(Intent(this, TelaMapas::class.java))
            R.id.MudarRaio -> mudarRaio()
        }

        if(NivelAcesso == Chave.CHAVE_ADM.valor){
            if(R.id.AdicionarLoja == item?.itemId){
                startActivity(Intent(this, TelaAdicionarLoja::class.java))
            }
        }

        return super.onOptionsItemSelected(item!!)
    }

    private fun mudarRaio(){
        val alertaCarregando = Alertas.alertaCarregando(this)
        alertaCarregando.show()
        DatabaseFirebaseRecuperar.recuperaDadosPessoa(FirebaseAutenticacao.Autenticacao.currentUser?.email!!, object : DatabasePessoaInterface{
            override fun pessoaRecebida(Pessoa: Perfil) {
                alertaCarregando.dismiss()
                val Alerta = AlertDialog.Builder(this@TelaPrincipal)
                Alerta
                    .setView(R.layout.mudar_raio)
                    .setTitle(R.string.MudarRaio)
                    .setPositiveButton(R.string.Enviar){ _, _ ->
                        if(ProgressoRaio?.progress != Pessoa.raio.toInt()){
                            DatabaseFirebaseSalvar.mudarRaio(Pessoa.email, ProgressoRaio?.progress!!)

                        }
                    }.setNegativeButton(R.string.Cancelar, null)
                val AlertaFechado = Alerta.create()
                AlertaFechado.show()
                ProgressoRaio = AlertaFechado.findViewById<SeekBar>(R.id.ValorMudarRaio)
                ProgressoRaio?.progress = Pessoa.raio.toString().toInt()
            }
        })
    }

    private fun verificarGps(){
        if(!LocalizacaoPessoa.providerAtivo(this, LocationManager.GPS_PROVIDER)){
            val SnackGps = Snackbar.make(findViewById(R.id.TelaPrincipal), R.string.GpsDesligado, Snackbar.LENGTH_LONG)
            SnackGps.setAction(R.string.Sim) {
                val Iniciar = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(Iniciar)
            }.show()
        }
    }

    fun atualizarLista(){
        DatabaseFirebaseRecuperar.recuperaDadosPessoa(FirebaseAutenticacao.Autenticacao.currentUser?.email!!, object : DatabasePessoaInterface{
            override fun pessoaRecebida(Pessoa: Perfil) {
                val Raio = if(Pessoa.raio >= 5){
                    50.0
                } else {
                    Pessoa.raio.toDouble()
                }
                BuscarLojasProximas(this@TelaPrincipal, Raio / 100).procurarProximos(object : BuscarLojasProximas.BuscaConcluida{
                    override fun resultado(Modo: Boolean) {
                        BuscarLojasImagem(FirebaseAutenticacao.Autenticacao.currentUser?.email!!, object : BuscarLojasImagem.BuscarConcluida {
                            override fun concluido(Modo: Boolean, Lista: MutableList<Lojas>?, ListaImagem: HashMap<String, Bitmap>?, Pessoa: Perfil?) {
                                if(Lista != null  && ListaImagem != null){
                                    if(Lista.isNotEmpty()){
                                        ListaLocais.refazer(Lista)
                                        ListaBitmap.refazer(ListaImagem)
                                        TodosLocais.notificarMudanca()
                                    }
                                } else {
                                    TodosLocais.cancelarAtualizacao()
                                    Alertas.criarAlerter(this@TelaPrincipal, R.string.ErroLojas, R.string.Atencao).show()
                                }
                            }
                        })
                    }
                })
            }
        })

    }

}