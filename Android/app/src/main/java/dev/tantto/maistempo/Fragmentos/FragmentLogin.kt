package dev.tantto.maistempo.fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseUser
import dev.tantto.maistempo.chaves.Chave
import dev.tantto.maistempo.classes.Alertas
import dev.tantto.maistempo.classes.Dados
import dev.tantto.maistempo.google.AutenticacaoLogin
import dev.tantto.maistempo.google.FirebaseAutenticacao
import dev.tantto.maistempo.google.TiposErrosLogar
import dev.tantto.maistempo.R
import dev.tantto.maistempo.telas.TelaLogin
import java.util.regex.Pattern

class FragmentLogin: Fragment(), AutenticacaoLogin{

    private var referecencia:TelaLogin? = null

    private var UserName:EditText? = null
    private var Senha:EditText? = null
    private var SalvarUsuario:CheckBox? = null
    private var Conectar:Button? = null
    private var BotaoApresentacao:Button? = null
    private var BotaoNovo:Button? = null
    private var EsqueciSenha:Button? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val View = inflater.inflate(R.layout.fragment_login, container, false)
        recuperarView(View)
        return View
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(Chave.CHAVE_USERNAME.valor, UserName?.text.toString())
        outState.putString(Chave.CHAVE_SENHA.valor, Senha?.text.toString())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null){
            if(savedInstanceState.containsKey(Chave.CHAVE_USERNAME.valor) && savedInstanceState.containsKey(Chave.CHAVE_SENHA.valor)){
                UserName?.setText(savedInstanceState.getString(Chave.CHAVE_USERNAME.valor))
                Senha?.setText(savedInstanceState.getString(Chave.CHAVE_SENHA.valor))
            }
        }
    }

    private fun recuperarView(View: View) {
        UserName = View.findViewById(R.id.UserName)
        Senha = View.findViewById(R.id.Senha)
        SalvarUsuario = View.findViewById(R.id.LembrarLogin)
        Conectar = View.findViewById<Button>(R.id.ConectarUsuario)
        BotaoApresentacao = View.findViewById<Button>(R.id.IrApresentacao)
        BotaoNovo = View.findViewById<Button>(R.id.IrNovo)
        EsqueciSenha = View.findViewById<Button>(R.id.EsqueciSenha)

        eventos()
    }

    fun setandoReferencia(ref:TelaLogin) : FragmentLogin{
        referecencia = ref
        return this
    }

    private fun eventos() {
        BotaoApresentacao?.setOnClickListener {
            referecencia?.mudarTela(0)
        }
        BotaoNovo?.setOnClickListener {
            referecencia?.mudarTela(2)
        }
        Conectar?.setOnClickListener {
            verificar()
        }

        EsqueciSenha?.setOnClickListener {
            if(UserName?.text?.toString()?.isNotEmpty()!! && isEmailValido(UserName?.text?.toString()!!)){
                Alertas.criarAlerter(referecencia!!, R.string.EnviandoPedido    , R.string.Atencao, 5000).show()
                FirebaseAutenticacao.recuperarSenha(UserName?.text?.toString()!!, object : FirebaseAutenticacao.Mudanca{
                    override fun resultado(Modo: Boolean) {
                        if(Modo){
                            Alertas.criarAlerter(referecencia!!, R.string.EnviadoRecuperaSenha, R.string.Atencao, 10000).show()
                        } else {
                            Alertas.criarAlerter(referecencia!!, R.string.ErroEnviarRecupera, R.string.Atencao, 10000).show()
                        }
                    }
                })
            } else {
                Alertas.criarAlerter(referecencia!!, R.string.CampoEmailSenha, R.string.Atencao, 5000).show()
            }
        }
    }

    private fun verificar(){
        if(UserName?.text?.isEmpty()!!) {
            Alertas.criarAlerter(referecencia!!, R.string.EmailVazio, R.string.Atencao, 5000).show()
        } else if (Senha?.text?.isEmpty()!!){
            Alertas.criarAlerter(referecencia!!, R.string.SenhaVazia, R.string.Atencao, 5000).show()
        } else if(!UserName?.text?.toString()?.contains("@")!!){
            Alertas.criarAlerter(referecencia!!, R.string.ErroEmail, R.string.Atencao, 5000).show()
        } else if(!UserName?.text?.toString()?.contains(".com")!!){
            Alertas.criarAlerter(referecencia!!, R.string.ErroEmail, R.string.Atencao, 5000).show()
        } else if(Senha?.text?.length!! < 6){
            Alertas.criarAlerter(referecencia!!, R.string.ErroSenha, R.string.Atencao, 5000).show()
        } else if(referecencia != null){
            Alertas.criarAlerter(referecencia!!, R.string.AguardeConectando, R.string.Conectando, 30000).show()
            FirebaseAutenticacao.logarUsuario(UserName?.text.toString(), Senha?.text.toString(), this)
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

    override fun usuarioLogado(User: FirebaseUser?) {
        val contexto = this.context

        if(contexto != null){
            if(!SalvarUsuario?.isChecked!!){
                Dados.salvarLogin(true, contexto)
            } else {
                Dados.salvarLogin(false, contexto)
            }
        }

        referecencia?.loginConcluido(User)
    }

    override fun erroLogar(Erro: TiposErrosLogar) {
        Alertas.criarAlerter(referecencia!!, R.string.ErroLogar, R.string.Atencao, 5000).show()
    }
}