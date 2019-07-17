package dev.tantto.maistempo.Fragmentos

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseUser
import dev.tantto.maistempo.Classes.Alertas
import dev.tantto.maistempo.Google.AutenticacaoLogin
import dev.tantto.maistempo.Google.GoogleFirebaseAutenticacao
import dev.tantto.maistempo.Google.TiposErrosLogar
import dev.tantto.maistempo.R
import dev.tantto.maistempo.Telas.TelaLogin

@SuppressLint("ValidFragment")
class FragmentLogin(private var Contexto:Context, private var ReferecenciaTela:TelaLogin) : Fragment(), AutenticacaoLogin{

    var UserName:EditText? = null
    var Senha:EditText? = null
    var SalvarUsuario:CheckBox? = null
    var Conectar:Button? = null
    private var BotaoApresentacao:Button? = null
    private var BotaoNovo:Button? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val View = LayoutInflater.from(Contexto).inflate(R.layout.fragment_login, container, false)
        RecuperarView(View)
        return View
    }

    private fun RecuperarView(View: View) {
        UserName = View.findViewById(R.id.UserName)
        Senha = View.findViewById(R.id.Senha)
        SalvarUsuario = View.findViewById(R.id.LembrarLogin)
        Conectar = View.findViewById(R.id.Conectar)
        BotaoApresentacao = View.findViewById<Button>(R.id.IrApresentacao)
        BotaoNovo = View.findViewById<Button>(R.id.IrNovo)

        Eventos()

    }

    private fun Eventos() {
        Conectar?.setOnClickListener {
            Verificar()
        }

        BotaoApresentacao?.setOnClickListener {
            ReferecenciaTela.MudarTela(0)
        }
        BotaoNovo?.setOnClickListener {
            ReferecenciaTela.MudarTela(2)
        }
    }

    private fun Verificar(){
        if(UserName?.text?.isNotEmpty()!! && Senha?.text?.isNotEmpty()!!){
            GoogleFirebaseAutenticacao.LogarUsuario(UserName?.text.toString(), Senha?.text.toString(), this)
        }
    }

    override fun UsuarioLogado(User: FirebaseUser) {
        ReferecenciaTela.LoginConcluido(User)
    }

    override fun ErroLogar(Erro: TiposErrosLogar) {
        if(Erro == TiposErrosLogar.SENHA_INCORRETA){
            Alertas.CriarTela(ReferecenciaTela, R.string.SenhaIncorreta, R.string.Atencao, 5000).show()
        } else if(Erro == TiposErrosLogar.CONTA_NAO_EXISTENTE){
            Alertas.CriarTela(ReferecenciaTela, R.string.ContaNaoExistente, R.string.Atencao, 5000).show()
        }
    }
}