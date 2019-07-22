package dev.tantto.maistempo.Fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseUser
import dev.tantto.maistempo.Chaves.Chaves
import dev.tantto.maistempo.Classes.Alertas
import dev.tantto.maistempo.Google.AutenticacaoLogin
import dev.tantto.maistempo.Google.FirebaseAutenticacao
import dev.tantto.maistempo.Google.TiposErrosLogar
import dev.tantto.maistempo.R
import dev.tantto.maistempo.Telas.TelaLogin

class FragmentLogin: Fragment(), AutenticacaoLogin{

    private lateinit var ReferecenciaTela:TelaLogin

    private var UserName:EditText? = null
    private var Senha:EditText? = null
    private var SalvarUsuario:CheckBox? = null
    private var Conectar:Button? = null
    private var BotaoApresentacao:Button? = null
    private var BotaoNovo:Button? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val View = inflater.inflate(R.layout.fragment_login, container, false)
        recuperarView(View)
        return View
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(Chaves.CHAVE_USERNAME.valor, UserName?.text.toString())
        outState.putString(Chaves.CHAVE_SENHA.valor, Senha?.text.toString())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        /*if(savedInstanceState?.containsKey(Chaves.CHAVE_USERNAME.valor)!! && savedInstanceState.containsKey(Chaves.CHAVE_SENHA.valor)){
            UserName?.setText(savedInstanceState.getString(Chaves.CHAVE_USERNAME.valor))
            senha?.setText(savedInstanceState.getString(Chaves.CHAVE_SENHA.valor))
        }*/
    }

    private fun recuperarView(View: View) {
        UserName = View.findViewById(R.id.UserName)
        Senha = View.findViewById(R.id.Senha)
        SalvarUsuario = View.findViewById(R.id.LembrarLogin)
        Conectar = View.findViewById<Button>(R.id.ConectarUsuario)
        BotaoApresentacao = View.findViewById<Button>(R.id.IrApresentacao)
        BotaoNovo = View.findViewById<Button>(R.id.IrNovo)

        eventos()

    }

    fun setandoReferencia(ref:TelaLogin) : FragmentLogin{
        ReferecenciaTela = ref
        return this
    }

    private fun eventos() {
        BotaoApresentacao?.setOnClickListener {
            ReferecenciaTela.MudarTela(0)
        }
        BotaoNovo?.setOnClickListener {
            ReferecenciaTela.MudarTela(2)
        }
        Conectar?.setOnClickListener {
            verificar()
        }
    }

    private fun verificar(){
        if(UserName?.text?.isNotEmpty()!! && Senha?.text?.isNotEmpty()!!){
            FirebaseAutenticacao.logarUsuario(UserName?.text.toString(), Senha?.text.toString(), this)
        }
    }

    override fun usuarioLogado(User: FirebaseUser?) {
        ReferecenciaTela.LoginConcluido(User)
    }

    override fun erroLogar(Erro: TiposErrosLogar) {
        if(Erro == TiposErrosLogar.SENHA_INCORRETA){
            Alertas.CriarTela(ReferecenciaTela, R.string.SenhaIncorreta, R.string.Atencao, 5000).show()
        } else if(Erro == TiposErrosLogar.CONTA_NAO_EXISTENTE){
            Alertas.CriarTela(ReferecenciaTela, R.string.ContaNaoExistente, R.string.Atencao, 5000).show()
        }
    }
}