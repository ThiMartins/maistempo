package dev.tantto.maistempo.Google

import android.util.Log
import com.google.firebase.auth.*
import dev.tantto.maistempo.Modelos.Perfil

enum class TiposErrosCriar{
    SENHA_FRACA,
    CRENDENCIAL_INVALIDA,
    CONTA_EXISTENTE
}

enum class TiposErrosLogar{
    CONTA_NAO_EXISTENTE,
    SENHA_INCORRETA
}

class FirebaseAutenticacao{

    companion object {

        val Autenticacao:FirebaseAuth = FirebaseAuth.getInstance()

        fun logarUsuario(Email:String, Senha:String, Interface:AutenticacaoLogin){
            Autenticacao.signInWithEmailAndPassword(Email, Senha).addOnCompleteListener {
                if(it.isSuccessful){
                    Interface.usuarioLogado(it.result?.user!!)
                } else {
                    Log.i("Erro", it.exception.toString())
                    try {
                        throw it.exception!!
                    } catch (Erro:Throwable){
                        Erro.printStackTrace()
                    } catch (Erro:FirebaseAuthInvalidUserException){
                        Interface.erroLogar(TiposErrosLogar.SENHA_INCORRETA)
                    } catch (Erro:FirebaseAuthInvalidCredentialsException){
                        Interface.erroLogar(TiposErrosLogar.CONTA_NAO_EXISTENTE)
                    }
                }
            }
        }

        fun criarUsuario(Pessoa:Perfil, Interface:AutenticacaoCriar){
            Autenticacao.createUserWithEmailAndPassword(Pessoa.email, Pessoa.senha).addOnCompleteListener {
                if(it.isSuccessful){
                    DatabaseFirebaseSalvar.SalvarDados(Pessoa)
                    val User = it.result
                    Interface.usuarioCriado(User?.user, Pessoa)
                } else {
                    try {
                        throw it.exception!!
                    } catch (Erro:FirebaseAuthUserCollisionException){
                        Interface.erroCriarUsuario(TiposErrosCriar.CONTA_EXISTENTE)
                    }
                }
            }
        }

        fun deslogarUser(){
            Autenticacao.signOut()
        }
    }

}

interface AutenticacaoCriar{

    fun usuarioCriado(User:FirebaseUser?, Pessoa: Perfil)
    fun erroCriarUsuario(erro:TiposErrosCriar)

}

interface AutenticacaoLogin{

    fun usuarioLogado(User:FirebaseUser?)
    fun erroLogar(Erro:TiposErrosLogar)

}