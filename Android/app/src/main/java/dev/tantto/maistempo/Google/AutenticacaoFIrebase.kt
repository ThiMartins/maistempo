package dev.tantto.maistempo.google

import com.google.firebase.auth.*
import dev.tantto.maistempo.modelos.Perfil

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
                    try {
                        throw it.exception!!
                    } catch (Erro:FirebaseAuthException){
                        when(Erro.errorCode){
                            "ERROR_USER_NOT_FOUND" -> Interface.erroLogar(TiposErrosLogar.CONTA_NAO_EXISTENTE)
                            "ERROR_WRONG_PASSWORD" -> Interface.erroLogar(TiposErrosLogar.SENHA_INCORRETA)
                        }
                    }
                }
            }
        }

        fun criarUsuario(Pessoa:Perfil, Interface:AutenticacaoCriar){
            Autenticacao.createUserWithEmailAndPassword(Pessoa.email, Pessoa.senha).addOnCompleteListener {
                if(it.isSuccessful){
                    DatabaseFirebaseSalvar.salvarDados(Pessoa)
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

        fun apagarConta(){
            Autenticacao.currentUser?.delete()
        }

        fun mudarSenha(NovaSenha:String, Interface:Mudanca){
            Autenticacao.currentUser?.updatePassword(NovaSenha)?.addOnCompleteListener {
                if(it.isSuccessful){
                    Interface.resultado(true)
                }
            }
        }

        fun recuperarSenha(Email:String, Interface:Mudanca){
            Autenticacao.sendPasswordResetEmail(Email).addOnCompleteListener {
                if(it.isSuccessful){
                    Interface.resultado(true)
                } else {
                    Interface.resultado(false)
                }
            }
        }
    }

    interface Mudanca {

        fun resultado(Modo: Boolean)

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