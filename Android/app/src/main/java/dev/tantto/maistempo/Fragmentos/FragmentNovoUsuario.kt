package dev.tantto.maistempo.Fragmentos

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseUser
import dev.tantto.maistempo.Classes.*
import dev.tantto.maistempo.Google.*
import dev.tantto.maistempo.Modelos.Perfil
import dev.tantto.maistempo.R
import dev.tantto.maistempo.Telas.TelaLogin
import java.text.DateFormat
import java.util.*

class FragmentNovoUsuario : Fragment(), EnviarFotoCloud, AutenticacaoCriar{

    private lateinit var ReferenciaTela:TelaLogin

    private val RequisicaoPermissao = 2
    private val MODO_CAMERA = 0
    private val MODO_GALERIA = 1
    private val MODO_ARQUIVOS = 2

    private var Nome:EditText? = null
    private var Email:EditText? = null
    private var Senha:EditText? = null
    private var DataTexto:EditText? = null
    private var Foto:ImageView? = null
    private var Criar:Button? = null
    private var Data:Date = Date()
    private var CaminhoFoto:Uri? = null
    private var EscolherData:Button? = null
    private var FotoCamera:Bitmap? = null
    private var Cidade:EditText? = null
    private var CheckTermos:CheckBox? = null
    private var VerTermos:TextView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val Fragmento = inflater.inflate(R.layout.fragment_novo_usuario, container, false)
        configurandoView(Fragmento)
        eventos()
        return Fragmento
    }

    private fun configurandoView(Fragmento:View) {
        Nome = Fragmento.findViewById<EditText>(R.id.NomeNovo)
        Email = Fragmento.findViewById<EditText>(R.id.EmailNovo)
        Senha = Fragmento.findViewById<EditText>(R.id.SenhaNovo)
        Foto = Fragmento.findViewById<ImageView>(R.id.FotoNovoUsuario)
        DataTexto = Fragmento.findViewById<EditText>(R.id.DataNascimento)
        Criar = Fragmento.findViewById<Button>(R.id.CriarNovaConta)
        EscolherData = Fragmento.findViewById<Button>(R.id.AbrirDataPicker)
        Cidade = Fragmento.findViewById<EditText>(R.id.CidadeReferencia)
        CheckTermos = Fragmento.findViewById<CheckBox>(R.id.ConcordoTermos)
        VerTermos = Fragmento.findViewById<TextView>(R.id.VerTermos)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Foto?.clipToOutline = true
        }
    }

    fun setandoReferencia(ref:TelaLogin) : FragmentNovoUsuario{
        ReferenciaTela = ref
        return this
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == MODO_CAMERA && resultCode == Activity.RESULT_OK && data != null){
            val FotoSelecionada = data.extras?.get("data") as Bitmap
            FotoCamera = FotoSelecionada
            Foto?.setImageBitmap(FotoSelecionada)
        } else if(requestCode == MODO_GALERIA && resultCode == Activity.RESULT_OK && data != null){
            CaminhoFoto = data.data
            Foto?.setImageURI(CaminhoFoto)

        } else if(requestCode == MODO_ARQUIVOS && resultCode == Activity.RESULT_OK && data != null){
            CaminhoFoto = data.data
            Foto?.setImageURI(CaminhoFoto)

        }
    }

    @Suppress("DEPRECATION")
    private fun eventos() {
        Criar?.setOnClickListener {
            verificar()
        }

        VerTermos?.setOnClickListener {
            AlertDialog
                .Builder(this.requireContext())
                .setView(R.layout.termos_leitura)
                .setTitle(R.string.TermosDeUso)
                .setPositiveButton(R.string.Ok, null)
                .create()
                .show()
        }

        EscolherData?.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val DataNascimento = DatePickerDialog(this.requireContext())
                DataNascimento.show()
                DataNascimento.setOnDateSetListener { _, year, month, dayOfMonth ->
                    Data = Date(year - 1900, month, dayOfMonth)
                    val DataFormatada = DateFormat.getDateInstance().format(Data)
                    DataTexto?.setText(DataFormatada)
                }
            } else {
                Toast.makeText(this.requireContext(), "Refatorando layout", Toast.LENGTH_LONG).show()
            }

        }

        Foto?.setOnClickListener {
            if(Permissao.veficarPermissao(ReferenciaTela, Permissoes.CAMERA) != TipoDePermissao.PERMITIDO){
                ActivityCompat.requestPermissions(ReferenciaTela, arrayOf(Manifest.permission.CAMERA), RequisicaoPermissao)
            } else {
                pegarFoto()
            }

        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            RequisicaoPermissao -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    pegarFoto()
                } else{
                    Alertas.criarAlerter(ReferenciaTela, R.string.ErroPermissaoFoto, R.string.Atencao, 5000)
                }
            }
        }
    }

    private fun alerta(Mensagem:Int, Titulo:Int, Duracao:Long = 10000) {
        Alertas.criarAlerter(ReferenciaTela, Mensagem, Titulo, Duracao).show()
    }

    private fun pegarFoto() {
        val Caixa = AlertDialog.Builder(this.requireContext())
        Caixa.setTitle(R.string.Escolha)
        Caixa.setItems(arrayOf(getString(R.string.Camera), getString(R.string.Galeria), getString(R.string.Arquivos))) { _, which ->
            val Iniciar = Intent()
            when (which) {
                0 -> {
                    Iniciar.action = MediaStore.ACTION_IMAGE_CAPTURE
                    startActivityForResult(Iniciar, MODO_CAMERA)
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

    private fun verificar() {
        if(Nome?.text?.isNotEmpty()!! ){
            if (Email?.text?.toString()?.contains("@")!! && Email?.text.toString().contains(".com")){
                if (Senha?.text?.toString()?.length!! >= 6){
                    if(Cidade?.text.toString().isNotEmpty()){
                        if(CheckTermos?.isChecked!!){
                            alerta(R.string.Atencao, R.string.Aguarde)
                            when {
                                CaminhoFoto != null -> CloudStorageFirebase.salvarFotoCloud(CaminhoFoto, Email?.text.toString(), this)
                                FotoCamera != null -> {
                                    CaminhoFoto = bitmapUtils.getImageUri(FotoCamera!!, Email?.text.toString(), ReferenciaTela)
                                    CloudStorageFirebase.salvarFotoCloud(CaminhoFoto, Email?.text.toString(), this)
                                }
                                else -> criarUsuario()
                            }
                        } else {
                            alerta(R.string.FaltaTermos, R.string.Atencao)
                            CheckTermos?.requestFocus()
                        }
                    } else {
                        alerta(R.string.CidadeFaltando, R.string.Atencao)
                        Cidade?.requestFocus()
                    }
                } else {
                    alerta(R.string.ErroSenha, R.string.Atencao)
                    Senha?.requestFocus()
                }
            } else {
                alerta(R.string.ErroEmail, R.string.Atencao)
                Email?.requestFocus()
            }
        } else {
            alerta(R.string.ErroNome, R.string.Atencao)
            Nome?.requestFocus()
        }
    }

    private fun criarUsuario() {
        FirebaseAutenticacao.criarUsuario(
            Perfil(
                titulo = Nome?.text.toString(),
                email = Email?.text.toString(),
                senha = Senha?.text.toString(),
                nascimento = DataTexto?.text.toString(),
                cidade = Cidade?.text.toString().toLowerCase()
            ), this
        )
    }

    override fun usuarioCriado(User: FirebaseUser?, Pessoa: Perfil) {
        ReferenciaTela.loginConcluido(User, Pessoa)
    }

    override fun erroCriarUsuario(erro: TiposErrosCriar) {
        when (erro) {
            TiposErrosCriar.SENHA_FRACA -> alerta(R.string.ExceptionSenhaFraca, R.string.Atencao, 5000)
            TiposErrosCriar.CRENDENCIAL_INVALIDA -> alerta(R.string.ExceptionEmailIncorrecto, R.string.Atencao, 5000)
            TiposErrosCriar.CONTA_EXISTENTE -> alerta(R.string.ExceptionContaExistente, R.string.Atencao, 5000)
        }
    }

    override fun enviadaSucesso() {
        criarUsuario()
    }

    override fun falhaEnviar(Erro: String) {
        alerta(R.string.ErroCriar, R.string.Atencao, 5000)
    }

    override fun enviarProgresso(Progresso: Double) {
        //Mostrar o andamento do envio da imagem
    }
}
