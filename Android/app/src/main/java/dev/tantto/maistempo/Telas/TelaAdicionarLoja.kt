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
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import dev.tantto.maistempo.Classes.*
import dev.tantto.maistempo.Modelos.Lojas
import dev.tantto.maistempo.R
import dev.tantto.maistempo.google.DatabaseFirebaseSalvar
import org.json.JSONObject
import java.lang.Exception
import org.json.JSONArray
import org.json.JSONException

class TelaAdicionarLoja : AppCompatActivity() {

    private var Imagem:ImageView? = null
    private var Nome:EditText? = null
    private var Local:EditText? = null
    private var Telefone:EditText? = null
    private var Latitude:EditText? = null
    private var Longitude:EditText? = null
    private var Cidade:EditText? = null
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
        Imagem = findViewById<ImageView>(R.id.FotoNovoLoja)
        Nome = findViewById<EditText>(R.id.NomeNovoLoja)
        Local = findViewById<EditText>(R.id.LocalNovoLoja)
        Telefone = findViewById<EditText>(R.id.TelefoneNovoLoja)
        Latitude = findViewById<EditText>(R.id.LatitudeLoja)
        Longitude = findViewById<EditText>(R.id.LongitudeLoja)
        Cidade = findViewById<EditText>(R.id.CidadeReferenciaLoja)
        AdicionarImage = findViewById<TextView>(R.id.AddImagemLoja)
        AdicionarViaMapa = findViewById<Button>(R.id.AbrirMapaLoja)
        AdicionarLoja = findViewById<Button>(R.id.CriarNovaLoja)
        HorarioInicio = findViewById<EditText>(R.id.HorarioInicioLoja)
        HorarioFim = findViewById<EditText>(R.id.HorarioFimLoja)

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
            Cidade?.text?.toString()?.isEmpty()!! -> {
                alerta(R.string.ErroCidadeLoja, R.string.Atencao)
                Cidade?.requestFocus()
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
        DatabaseFirebaseSalvar.adicionarLoja(Lojas(
            id = "",
            titulo = Nome?.text?.toString()!!,
            latitude = Latitude?.text?.toString()?.toDouble()!!,
            longitude = Longitude?.text?.toString()?.toDouble()!!,
            local = Local?.text?.toString()!!,
            filaNormal = hashMapOf(),
            filaRapida = hashMapOf(),
            filaPreferencial = hashMapOf(),
            cidade = Cidade?.text?.toString()?.toLowerCase()!!,
            telefone = Telefone?.text?.toString()!!,
            quantidadeAvaliacoesFila = 0,
            quantidadeAvaliacoesRating =  0,
            horarioInicio = HorarioInicio?.text?.toString()?.toInt()!!,
            horarioFinal =  HorarioFim?.text?.toString()?.toInt()!!,
            mediaRanking = 0.0
        ), CaminhoFoto!!)
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

                } else {
                    Alerta.dismiss()
                }
            }
        })
        download.execute(("Rua Américo Figuereido, 547, Sorocaba").replace(" ","+"))

    }

    private fun exibirCaixa() {
        val Caixa = AlertDialog.Builder(this)
        Caixa.setTitle(R.string.Escolha)
        Caixa.setItems(arrayOf(getString(R.string.Camera), getString(R.string.Galeria), getString(R.string.Arquivos))) { _, which ->
            val Iniciar = Intent()
            when (which) {
                0 -> {
                    if(Permissao.veficarPermissao(this, Permissoes.CAMERA) == TipoDePermissao.PERMITIDO){
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
            CaminhoFoto = BitmapUtilitarios.getImageUri(FotoSelecionada, "loja", this)
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

                Log.i("Teste", lat)
                Log.i("Teste", lng)
                Log.i("Teste", json.toString())

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
