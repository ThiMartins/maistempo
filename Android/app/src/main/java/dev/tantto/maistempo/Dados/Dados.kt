package dev.tantto.maistempo.Dados

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseUser

class Dados(private val Contexto:Context) {

    companion object {
        private val NOME_TABELA = "ValoresTempo"
        private val EMAIL = "EMAIL"
        private val SENHA = "SENHA"
        private val LOGADO = "LOGADO"
    }

    fun SalvarLogin(Email:String, Senha:String){
        val Preferencia = RecuperarPreferencias()
        val Editor = Preferencia?.edit()
        Editor?.putString(EMAIL, Email)
        Editor?.putString(SENHA, Senha)
        Editor?.putBoolean(LOGADO, true)
        Editor?.apply()
    }

    private fun RecuperarPreferencias(): SharedPreferences? {
        val Preferencia = Contexto.getSharedPreferences(NOME_TABELA, 0)
        return Preferencia
    }

    fun Logado() : Boolean{
        val Preferencia = RecuperarPreferencias()
        return Preferencia?.getBoolean(LOGADO, false)!!
    }

}