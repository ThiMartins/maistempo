package dev.tantto.maistempo.Google

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dev.tantto.maistempo.Modelos.Perfil

class GoogleFirebaseAutenticacao {


    companion object {

        val Autenticacao:FirebaseAuth = FirebaseAuth.getInstance()

        fun LogarUsuario(Email:String, Senha:String){

        }

        fun CriarUsuario(Pessoa:Perfil, Interface:Autenticacao){
            Autenticacao.createUserWithEmailAndPassword(Pessoa.Email, Pessoa.Senha).addOnCompleteListener {
                if(it.isSuccessful){
                    GoogleFirebaseRealtimeDatabase.SalvarDados(Pessoa)
                    val Pessoa = it.result
                    Interface.UsuarioCriado(Pessoa?.user)
                } else {
                    try {
                        throw it.exception!!
                        //Seila
                    }
                }
            }
        }
    }

}

interface Autenticacao{

    fun UsuarioCriado(Pessoa:FirebaseUser?)
    fun ErroCriarUsuario()

}