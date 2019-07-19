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

        fun LogarUsuario(Email:String, Senha:String, Interface:AutenticacaoLogin){
            Autenticacao.signInWithEmailAndPassword(Email, Senha).addOnCompleteListener {
                if(it.isSuccessful){
                    Interface.UsuarioLogado(it.result?.user!!)
                } else {
                    Log.i("Erro", it.exception.toString())
                    try {
                        throw it.exception!!
                    } catch (Erro:FirebaseAuthInvalidUserException){
                        Interface.ErroLogar(TiposErrosLogar.SENHA_INCORRETA)
                    } catch (Erro:FirebaseAuthInvalidCredentialsException){
                        Interface.ErroLogar(TiposErrosLogar.CONTA_NAO_EXISTENTE)
                    }
                }
            }
        }

        fun CriarUsuario(Pessoa:Perfil, Interface:AutenticacaoCriar){
            Autenticacao.createUserWithEmailAndPassword(Pessoa.Email, Pessoa.Senha).addOnCompleteListener {
                if(it.isSuccessful){
                    DatabaseFirebaseSalvar.SalvarDados(Pessoa)
                    val User = it.result
                    Interface.UsuarioCriado(User?.user, Pessoa)
                } else {
                    try {
                        throw it.exception!!
                    } catch (Erro:FirebaseAuthUserCollisionException){
                        Interface.ErroCriarUsuario(TiposErrosCriar.CONTA_EXISTENTE)
                    }
                }
            }
        }
    }

}

interface AutenticacaoCriar{

    fun UsuarioCriado(User:FirebaseUser?, Pessoa: Perfil)
    fun ErroCriarUsuario(erro:TiposErrosCriar)

}

interface AutenticacaoLogin{

    fun UsuarioLogado(User:FirebaseUser?)
    fun ErroLogar(Erro:TiposErrosLogar)

}