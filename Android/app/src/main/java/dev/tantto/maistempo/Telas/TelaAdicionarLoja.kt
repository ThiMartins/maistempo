package dev.tantto.maistempo.telas

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.firebase.geofire.GeoLocation
import dev.tantto.maistempo.classes.*
import dev.tantto.maistempo.modelos.Lojas
import dev.tantto.maistempo.R
import dev.tantto.maistempo.google.CidadesRecuperadas
import dev.tantto.maistempo.google.DatabaseFirebaseRecuperar
import dev.tantto.maistempo.google.DatabaseFirebaseSalvar
import org.json.JSONObject
import java.lang.Exception
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException

class TelaAdicionarLoja : AppCompatActivity() {

    private var Imagem:ImageView? = null
    private var Nome:EditText? = null
    private var Local:EditText? = null
    private var Telefone:EditText? = null
    private var Latitude:EditText? = null
    private var Longitude:EditText? = null
    private var Cidade:Spinner? = null
    private var HorarioInicio:EditText? = null
    private var HorarioFim:EditText? = null
    private var AdicionarImage:TextView? = null
    private var AdicionarViaMapa:Button? = null
    private var AdicionarLoja:Button? = null

    private val MODO_CAMERA = 0
    private val MODO_GALERIA = 1
    private val MODO_ARQUIVOS = 2

    private var CaminhoFoto: Uri? = Uri.EMPTY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_adicionar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        configurandoElementos()
        eventos()

    }

    private fun configurandoElementos() {
        Imagem = findViewById(R.id.FotoNovoLoja)
        Nome = findViewById(R.id.NomeNovoLoja)
        Local = findViewById(R.id.LocalNovoLoja)
        Telefone = findViewById(R.id.TelefoneNovoLoja)
        Latitude = findViewById(R.id.LatitudeLoja)
        Longitude = findViewById(R.id.LongitudeLoja)
        Cidade = findViewById(R.id.CidadeReferenciaLoja)
        AdicionarImage = findViewById(R.id.AddImagemLoja)
        AdicionarViaMapa = findViewById(R.id.AbrirMapaLoja)
        AdicionarLoja = findViewById(R.id.CriarNovaLoja)
        HorarioInicio = findViewById(R.id.HorarioInicioLoja)
        HorarioFim = findViewById(R.id.HorarioFimLoja)

        val carregar = Alertas.alertaCarregando(this)
        carregar.show()

        DatabaseFirebaseRecuperar.recuperarCidades(object : CidadesRecuperadas {
            override fun listaCidades(Lista: List<String>?) {
                carregar.dismiss()
                if(Lista != null){
                    val adapter = ArrayAdapter(this@TelaAdicionarLoja, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, Lista)
                    Cidade?.adapter = adapter
                }
            }
        })

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Imagem?.clipToOutline = true
        }
    }

    private fun eventos() {
        AdicionarImage?.setOnClickListener {
            exibirCaixa()
        }

        AdicionarViaMapa?.setOnClickListener {
            donwload()
        }

        AdicionarLoja?.setOnClickListener {
            verificar()
        }
    }

    private fun verificar(){

        when {
            CaminhoFoto == Uri.EMPTY -> {
                alerta(R.string.ErroImagemLoja, R.string.Atencao)
                Imagem?.requestFocus()
            }
            Nome?.text?.toString()?.isEmpty()!! -> {
                alerta(R.string.ErroNomeLoja, R.string.Atencao)
                Nome?.requestFocus()
            }
            Local?.text?.toString()?.isEmpty()!! -> {
                alerta(R.string.ErroLocalLoja, R.string.Atencao)
                Local?.requestFocus()
            }
            Telefone?.text?.toString()?.isEmpty()!! -> {
                alerta(R.string.ErroTelefoneLojas, R.string.Atencao)
                Telefone?.requestFocus()
            }
            Latitude?.text?.toString()?.isEmpty()!! -> {
                alerta(R.string.ErroLatitude,R.string.Atencao)
                Latitude?.requestFocus()
            }
            Longitude?.text?.toString()?.isEmpty()!! -> {
                alerta(R.string.ErroLongitudeLoja, R.string.Atencao)
                Longitude?.requestFocus()
            }
            HorarioInicio?.text?.toString()?.isEmpty()!! ->{
                alerta(R.string.ErroHorarioInicioLoja, R.string.Atencao)
                HorarioInicio?.requestFocus()
            }
            HorarioFim?.text?.toString()?.isEmpty()!! -> {
                alerta(R.string.ErroHorarioFimLoja, R.string.Atencao)
                HorarioFim?.requestFocus()
            }
            else -> {
                criarLoja()
            }
        }
    }

    private fun criarLoja(){
        alerta(R.string.CriarLojaMensagem, R.string.Atencao)
        DatabaseFirebaseSalvar.adicionarLoja(Lojas(
            id = "",
            titulo = Nome?.text?.toString()!!,
            latitude = Latitude?.text?.toString()?.toDouble()!!,
            longitude = Longitude?.text?.toString()?.toDouble()!!,
            local = Local?.text?.toString()!!,
            filaNormal = hashMapOf(),
            filaRapida = hashMapOf(),
            filaPreferencial = hashMapOf(),
            cidade = Cidade?.selectedItem?.toString()!!,
            telefone = Telefone?.text?.toString()!!,
            quantidadeAvaliacoesFila = 0,
            quantidadeAvaliacoesRating =  0,
            horarioInicio = HorarioInicio?.text?.toString()?.toInt()!!,
            horarioFinal =  HorarioFim?.text?.toString()?.toInt()!!,
            mediaRanking = 0.0
        ), CaminhoFoto!!, GeoLocation(Latitude?.text?.toString()?.toDouble()!!, Longitude?.text?.toString()?.toDouble()!!), object : DatabaseFirebaseSalvar.CriarLoja{
            override fun resultado(Modo: Boolean) {
                if(Modo){
                    this@TelaAdicionarLoja.finish()
                } else {
                    alerta(R.string.ErroCriarLoja, R.string.Atencao)
                }
            }
        })

    }

    private fun alerta(Mensagem:Int, Titulo:Int, Duracao:Long = 10000) {
        Alertas.criarAlerter(this, Mensagem, Titulo, Duracao).show()
    }

    private fun donwload(){
        val AlertaBuilder = AlertDialog.Builder(this)
        AlertaBuilder.setView(R.layout.loading)
        val Alerta = AlertaBuilder.create()
        Alerta.window?.setBackgroundDrawableResource(R.color.Transparente)
        Alerta.show()
        val download = RecuperarCoordenadas(object  : Recuperado{
            override fun recuperado(Modo: Boolean, Latitude: String?, Longitude: String?) {
                if(Modo){
                    this@TelaAdicionarLoja.Latitude?.setText(Latitude)
                    this@TelaAdicionarLoja.Longitude?.setText(Longitude)
                    Alerta.dismiss()
                } else {
                    Alerta.dismiss()
                }
            }
        })
        download.execute((Local?.text?.toString() + ", ${Cidade?.selectedItem?.toString()}").replace(" ","+"))

    }

    private fun exibirCaixa() {
        val Caixa = AlertDialog.Builder(this)
        Caixa.setTitle(R.string.Escolha)
        Caixa.setItems(arrayOf(getString(R.string.Camera), getString(R.string.Galeria), getString(R.string.Arquivos))) { _, which ->
            val Iniciar = Intent()
            when (which) {
                0 -> {
                    if(Permissao.veficarPermissao(this, Permissao.Permissoes.CAMERA) == Permissao.TipoDePermissao.PERMITIDO){
                        Iniciar.action = MediaStore.ACTION_IMAGE_CAPTURE
                        startActivityForResult(Iniciar, MODO_CAMERA)
                    } else {
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 2)
                    }
                }
                1 -> {
                    Iniciar.type = "image/*"
                    Iniciar.action = Intent.ACTION_PICK
                    startActivityForResult(Iniciar, MODO_GALERIA)
                }
                2 -> {
                    Iniciar.type = "image/*"
                    Iniciar.action = Intent.ACTION_GET_CONTENT
                    startActivityForResult(Iniciar, MODO_ARQUIVOS)
                }
            }
        }
        Caixa.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == MODO_CAMERA && resultCode == Activity.RESULT_OK && data != null){
            val FotoSelecionada = data.extras?.get("data") as Bitmap
            try {
                CaminhoFoto = BitmapUtilitarios.getImageUri(FotoSelecionada, "loja", this)
            } catch (Erro:IOException){
                Erro.printStackTrace()
            }
            Imagem?.setImageBitmap(FotoSelecionada)

        } else if(requestCode == MODO_GALERIA && resultCode == Activity.RESULT_OK && data != null){
            CaminhoFoto = data.data
            Imagem?.setImageURI(CaminhoFoto)

        } else if(requestCode == MODO_ARQUIVOS && resultCode == Activity.RESULT_OK && data != null){
            CaminhoFoto = data.data
            Imagem?.setImageURI(CaminhoFoto)
        }
    }

    private class RecuperarCoordenadas(val anInterface:Recuperado) : AsyncTask<String, Void, String?>(){

        override fun doInBackground(vararg p0: String?): String? {
            var Resposta:String? = null
            try {

                val Endereco = p0[0]
                val url = String.format("https://maps.googleapis.com/maps/api/geocode/json?address=$Endereco")
                Resposta = HttpDataHandler().recuperarHttpData(url)
                return Resposta

            } catch (Erro:Exception){

            }
            return Resposta

        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                Log.i("Teste", result.toString())
                val json = JSONObject(result!!)

                val lat = (json.get("results") as JSONArray).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lat").toString()
                val lng = (json.get("results") as JSONArray).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lng").toString()

                anInterface.recuperado(true, lat, lng)

            } catch (Erro:JSONException){
                anInterface.recuperado(false, null, null)
                Erro.printStackTrace()
            }
        }
    }

    interface Recuperado{

        fun recuperado(Modo:Boolean, Latitude:String?, Longitude:String?)

    }

}

