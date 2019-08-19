package dev.tantto.maistempo.fragmentos

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
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseUser
import dev.tantto.maistempo.classes.*
import dev.tantto.maistempo.google.*
import dev.tantto.maistempo.modelos.Perfil
import dev.tantto.maistempo.R
import dev.tantto.maistempo.telas.TelaLogin
import java.io.IOException
import java.text.DateFormat
import java.util.*
import java.util.regex.Pattern

class FragmentNovoUsuario : Fragment(), EnviarFotoCloud, AutenticacaoCriar{

    private lateinit var referenciaTela:TelaLogin

    private val RequisicaoPermissao = 2
    private val MODO_CAMERA = 0
    private val MODO_GALERIA = 1
    private val MODO_ARQUIVOS = 2

    private var Nome:EditText? = null
    private var Email:EditText? = null
    private var Senha:EditText? = null
    private var SenhaConfirmar:EditText? = null
    private var SenhaConfirmarInput:TextInputLayout? = null
    private var DataTexto:EditText? = null
    private var DataTextoInput:TextInputLayout? = null
    private var Foto:ImageView? = null
    private var Criar:Button? = null
    private var CaminhoFoto:Uri? = null
    private var EscolherData:Button? = null
    private var FotoCamera:Bitmap? = null
    private var Cidade:Spinner? = null
    private var CheckTermos:CheckBox? = null
    private var VerTermos:TextView? = null
    private var AnoSpinner:Spinner? = null
    private var MesSpinner:Spinner? = null
    private var DiaSpinner:Spinner? = null
    private var BackupData:String = ""
    private var ListaCidades:MutableList<String> = mutableListOf()

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
        SenhaConfirmar = Fragmento.findViewById<EditText>(R.id.SenhaConfirmarNovo)
        SenhaConfirmarInput = Fragmento.findViewById<TextInputLayout>(R.id.SenhaConfirmarInputLayout)
        Foto = Fragmento.findViewById<ImageView>(R.id.FotoNovoUsuario)
        DataTexto = Fragmento.findViewById<EditText>(R.id.DataNascimento)
        DataTextoInput = Fragmento.findViewById<TextInputLayout>(R.id.DataInputLayout)
        Criar = Fragmento.findViewById<Button>(R.id.CriarNovaConta)
        EscolherData = Fragmento.findViewById<Button>(R.id.AbrirDataPicker)
        Cidade = Fragmento.findViewById<Spinner>(R.id.CidadeReferencia)
        CheckTermos = Fragmento.findViewById<CheckBox>(R.id.ConcordoTermos)
        VerTermos = Fragmento.findViewById<TextView>(R.id.VerTermos)

        configuraAdapter()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Foto?.clipToOutline = true
        }
    }

    fun passandoCidades(Lista:List<String>){
        ListaCidades.addAll(Lista)
        configuraAdapter()
    }

    fun configuraAdapter(){
        val contexto = this@FragmentNovoUsuario.context
        if(contexto != null){
            val adapter = ArrayAdapter(contexto, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, ListaCidades)
            Cidade?.adapter = adapter
        }
    }

    fun setandoReferencia(ref:TelaLogin) : FragmentNovoUsuario{
        referenciaTela = ref
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

    private fun eventos() {

        SenhaConfirmar?.addTextChangedListener(object  : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(p0?.toString() != Senha?.text?.toString()){
                    SenhaConfirmarInput?.error = getString(R.string.SenhaErrada)
                } else {
                    SenhaConfirmarInput?.isErrorEnabled = false
                }
            }
        })

        DataTexto?.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                if(p0 != null){
                    BackupData = p0.toString()
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(p0 != null){
                    if(p0.length == 2 && BackupData.length < p0.length){
                        DataTexto?.setText(String.format("$p0/"))
                        DataTexto?.setSelection(3)
                        val Dia = String.format("${p0[0]}${p0[1]}")
                        if (Dia.toInt() > 31){
                            DataTextoInput?.error = getString(R.string.ErroDia)
                        } else {
                            DataTextoInput?.isErrorEnabled = false
                        }
                    } else if (p0.length == 5 && BackupData.length < p0.length){
                        DataTexto?.setText(String.format("$p0/"))
                        DataTexto?.setSelection(6)
                        val Mes = String.format("${p0[3]}${p0[4]}")
                        if (Mes.toInt() > 12){
                            DataTextoInput?.error = getString(R.string.ErroMes)
                        } else {
                            DataTextoInput?.isErrorEnabled = false
                        }
                    } else {
                        BackupData = DataTexto?.text?.toString()!!
                        DataTextoInput?.isErrorEnabled = false
                    }
                }
            }
        })

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
                DataNascimento.updateDate(Calendar.getInstance().get(Calendar.YEAR) - 18, Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
                DataNascimento.show()
                DataNascimento.setOnDateSetListener { _, year, month, dayOfMonth ->
                    val ConverterData = Calendar.getInstance()
                    ConverterData.set(year, month, dayOfMonth)
                    val DataFormatada = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(ConverterData.time)
                    DataTexto?.setText(DataFormatada)
                }
            } else {
                val TelaData = Alertas.criarAlertDialog(this.context!!)
                TelaData.setPositiveButton(R.string.Ok) { _, _ ->
                    val Dia = DiaSpinner?.selectedItem.toString().toInt()
                    val Mes = MesSpinner?.selectedItem.toString().toInt()
                    val Ano = AnoSpinner?.selectedItem.toString().toInt()

                    val ConverterData = Calendar.getInstance()
                    ConverterData.set(Ano, Mes, Dia)
                    val DataFormatada = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(ConverterData.time)
                    DataTexto?.setText(DataFormatada)
                }
                TelaData.setNegativeButton(R.string.Cancelar) { _, _ ->

                }
                val TelaFinal = TelaData.create()
                TelaFinal.show()

                val AdaptadorAno = ArrayAdapter(this.requireContext(), android.R.layout.simple_spinner_dropdown_item, criarArray( 1900, Calendar.getInstance().get(Calendar.YEAR)).reversed())
                val AdaptadorMes= ArrayAdapter(this.requireContext(), android.R.layout.simple_spinner_dropdown_item, criarArray(1, 12))
                val AdaptadorDia = ArrayAdapter(this.requireContext(), android.R.layout.simple_spinner_dropdown_item, criarArray(1, 31))

                AnoSpinner = TelaFinal.findViewById<Spinner>(R.id.AnoSpinner)
                MesSpinner = TelaFinal.findViewById<Spinner>(R.id.MesSpinner)
                DiaSpinner = TelaFinal.findViewById<Spinner>(R.id.DiaSpinner)

                AnoSpinner?.adapter = AdaptadorAno
                MesSpinner?.adapter = AdaptadorMes
                DiaSpinner?.adapter = AdaptadorDia

            }

        }

        Foto?.setOnClickListener {
            if(Permissao.veficarPermissao(referenciaTela, Permissao.Permissoes.CAMERA) != Permissao.TipoDePermissao.PERMITIDO){
                ActivityCompat.requestPermissions(referenciaTela, arrayOf(Manifest.permission.CAMERA), RequisicaoPermissao)
            } else {
                pegarFoto()
            }

        }

    }

    private fun criarArray(Inicio:Int, Fim:Int) : List<Int>{
        val Lista = mutableListOf<Int>()
        for (i in Inicio..Fim ){
            Lista.add(i)
        }
        return Lista.toList()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            RequisicaoPermissao -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    pegarFoto()
                } else{
                    Alertas.criarAlerter(referenciaTela, R.string.ErroPermissaoFoto, R.string.Atencao, 5000)
                }
            }
        }
    }

    private fun alerta(Mensagem:Int, Titulo:Int, Duracao:Long = 10000) {
        Alertas.criarAlerter(referenciaTela, Mensagem, Titulo, Duracao).show()
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
        Caixa.create().show()
    }

    private fun verificar() {
        if(Nome?.text?.isNotEmpty()!! ){
            if (isEmailValido(Email?.text?.toString()!!)){
                if (Senha?.text?.toString()?.length!! >= 6 && SenhaConfirmar?.text?.toString() == Senha?.text?.toString()){
                    if(Cidade?.selectedItem.toString().isNotEmpty()){
                        if(CheckTermos?.isChecked!!){
                            alerta(R.string.Atencao, R.string.Aguarde)
                            when {
                                CaminhoFoto != null -> CloudStorageFirebase.salvarFotoCloud(CaminhoFoto, Email?.text.toString(), this)
                                FotoCamera != null -> {
                                    try {
                                        CaminhoFoto = BitmapUtilitarios.getImageUri(FotoCamera!!, Email?.text.toString(), referenciaTela)
                                        CloudStorageFirebase.salvarFotoCloud(CaminhoFoto, Email?.text.toString(), this)
                                    } catch (Erro:IOException){
                                        Erro.printStackTrace()
                                    }
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
                } else if(Senha?.text?.toString()?.length!! >= 6){
                    alerta(R.string.ErroSenha, R.string.Atencao)
                    Senha?.requestFocus()
                }
                else if (SenhaConfirmar?.text?.toString() != Senha?.text?.toString()){
                    alerta(R.string.SenhaErrada, R.string.Atencao)
                    SenhaConfirmar?.requestFocus()
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

    private fun isEmailValido(Email:String) : Boolean{
        return Pattern.compile(
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        ).matcher(Email).matches()
    }

    private fun criarUsuario() {
        FirebaseAutenticacao.criarUsuario(
            Perfil(
                titulo = Nome?.text.toString(),
                email = Email?.text.toString(),
                senha = Senha?.text.toString(),
                nascimento = DataTexto?.text.toString(),
                cidade = Cidade?.selectedItem.toString()
            ), this
        )
    }

    override fun usuarioCriado(User: FirebaseUser?, Pessoa: Perfil) {
        referenciaTela.loginConcluido(User, Pessoa)
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
