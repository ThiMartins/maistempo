package dev.tantto.maistempo.Fragmentos

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
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
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseUser
import dev.tantto.maistempo.Classes.Alertas
import dev.tantto.maistempo.Google.*
import dev.tantto.maistempo.Modelos.Perfil
import dev.tantto.maistempo.R
import dev.tantto.maistempo.Telas.TelaLogin
import java.text.DateFormat
import java.util.*

class FragmentNovoUsuario : Fragment(), EnviarFotoCloud, AutenticacaoCriar{

    private lateinit var Contexto:Context
    private lateinit var ReferenciaTela:TelaLogin

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val Fragmento = inflater.inflate(R.layout.fragment_novo_usuario, container, false)

        ConfigurandoView(Fragmento)
        Eventos()

        return Fragmento
    }

    private fun ConfigurandoView(Fragmento:View) {
        Nome = Fragmento.findViewById<EditText>(R.id.NomeNovo)
        Email = Fragmento.findViewById<EditText>(R.id.EmailNovo)
        Senha = Fragmento.findViewById<EditText>(R.id.SenhaNovo)
        Foto = Fragmento.findViewById<ImageView>(R.id.FotoNovoUsuario)
        DataTexto = Fragmento.findViewById<EditText>(R.id.DataNascimento)
        Criar = Fragmento.findViewById<Button>(R.id.NovaConta)
        EscolherData = Fragmento.findViewById<Button>(R.id.AbrirDataPicker)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Foto?.clipToOutline = true
        }
    }

    fun setandoReferencia(Contexto:Context, ref:TelaLogin) : FragmentNovoUsuario{
        this.Contexto = Contexto
        ReferenciaTela = ref
        return this
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == MODO_CAMERA && resultCode == Activity.RESULT_OK && data != null){
            val FotoSelecionada = data.extras.get("data") as Bitmap
            CaminhoFoto = data.data
            Foto?.setImageBitmap(FotoSelecionada)

        } else if(requestCode == MODO_GALERIA && resultCode == Activity.RESULT_OK && data != null){
            CaminhoFoto = data.data
            Foto?.setImageURI(CaminhoFoto)

        } else if(requestCode == MODO_ARQUIVOS && resultCode == Activity.RESULT_OK && data != null){
            CaminhoFoto = data.data
            Foto?.setImageURI(CaminhoFoto)

        }
    }

    private fun Eventos() {
        Criar?.setOnClickListener {
            Verificar()
        }

        EscolherData?.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val DataNascimento = DatePickerDialog(Contexto)
                DataNascimento.show()
                DataNascimento.setOnDateSetListener { view, year, month, dayOfMonth ->
                    Data = Date(year, month, dayOfMonth)
                    val DataFormatada = DateFormat.getDateInstance().format(Data)
                    DataTexto?.setText(DataFormatada)
                }
            } else {
                TODO("Fazer o date picker para versoes menores que N")
            }
        }

        Foto?.setOnClickListener {
            PegarFoto()
        }

    }

    private fun Alerta(Mensagem:Int, Titulo:Int, Duracao:Long = 10000) {
        Alertas.CriarTela(ReferenciaTela, Mensagem, Titulo, Duracao).show()
    }

    private fun PegarFoto() {
        val Caixa = AlertDialog.Builder(Contexto)
        Caixa.setTitle(R.string.Escolha)
        Caixa.setItems(arrayOf("Camera", "Galeria", "Arquivos"), DialogInterface.OnClickListener { dialog, which ->
            val Iniciar = Intent()
            if (which == 0) {
                Iniciar.action = MediaStore.ACTION_IMAGE_CAPTURE
                startActivityForResult(Iniciar, MODO_CAMERA)
            } else if (which == 1) {
                Iniciar.type = "image/*"
                Iniciar.action = Intent.ACTION_PICK
                startActivityForResult(Iniciar, MODO_GALERIA)
            } else if (which == 2) {
                Iniciar.type = "image/*"
                Iniciar.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(Iniciar, MODO_ARQUIVOS)
            }
        })
        Caixa.show()
    }

    private fun Verificar() {
        if(Nome?.text?.isNotEmpty()!! ){
            if (Email?.text?.contains("@")!!){
                if (Senha?.text?.toString()?.length!! > 6){
                    Alerta(R.string.Atencao, R.string.Aguarde)
                    if(CaminhoFoto != null){
                        CloudStorageFirebase.SalvarFotoCloud(CaminhoFoto, Email?.text.toString(), this)
                    } else {
                        CriarUsuario()
                    }
                } else {
                    Alerta(R.string.ErroSenha, R.string.Atencao)
                }
            } else {
                Alerta(R.string.ErroEmail, R.string.Atencao)
            }
        } else {
            Alerta(R.string.ErroNome, R.string.Atencao)
        }
    }

    private fun CriarUsuario(Valor:String = "") {
        FirebaseAutenticacao.CriarUsuario(
            Perfil(
                Titulo = Nome?.text.toString(),
                Email = Email?.text.toString(),
                Senha = Senha?.text.toString(),
                Imagem = Valor,
                Nascimento = DataTexto?.text.toString()
            ), this
        )
    }

    override fun UsuarioCriado(User: FirebaseUser?, Pessoa: Perfil) {
        //Salvar SharedPreferences
        ReferenciaTela.LoginConcluido(User, Pessoa)
    }

    override fun ErroCriarUsuario(erro: TiposErrosCriar) {
        if(erro == TiposErrosCriar.SENHA_FRACA){
            Alerta(R.string.ExceptionSenhaFraca, R.string.Atencao, 5000)
        } else if (erro == TiposErrosCriar.CRENDENCIAL_INVALIDA){
            Alerta(R.string.ExceptionEmailIncorrecto, R.string.Atencao, 5000)
        } else if (erro == TiposErrosCriar.CONTA_EXISTENTE){
            Alerta(R.string.ExceptionContaExistente, R.string.Atencao, 5000)
        }
    }

    override fun EnviadaSucesso(Foto:Uri?) {
        CriarUsuario(Foto.toString())
    }

    override fun FalhaEnviar(Erro: String) {
        Alerta(R.string.ErroCriar, R.string.Atencao, 5000)
    }

    override fun EnviarProgresso(Progresso: Double) {
        //Mostrar o andamento do envio da imagem
    }
}
