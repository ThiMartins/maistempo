package dev.tantto.maistempo.Fragmentos

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dev.tantto.maistempo.Interfaces.EnumTelas
import dev.tantto.maistempo.R
import dev.tantto.maistempo.Telas.TelaLogin

@SuppressLint("ValidFragment")
class FragmentLogin(private var Contexto:Context, private var ReferecenciaTela:TelaLogin) : Fragment(){

    var UserName:EditText? = null
    var Senha:EditText? = null
    var SalvarUsuario:CheckBox? = null
    var Conectar:Button? = null
    var ConectarGoole:SignInButton? = null

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
        ConectarGoole = View.findViewById(R.id.ConectarGoogle)

        Conectar?.setOnClickListener {
            Verificar()
        }

        ConectarGoole?.setOnClickListener {
        }

    }

    private fun Verificar(){
        if(UserName?.text?.isNotEmpty()!! && Senha?.text?.isNotEmpty()!!){

        }
    }
}