package dev.tantto.maistempo.Dados

import android.content.Context
import android.content.SharedPreferences

class Dados(private val Contexto:Context) {

    companion object {
        private const val NOME_TABELA = "ValoresTempo"
        private const val EMAIL = "EMAIL"
        private const val SENHA = "SENHA"
        private const val CIDADE = "CIDADE"
    }

    fun salvarLogin(Email:String, Senha:String, Cidade:String){
        val Preferencia = recuperarPreferencias()
        val Editor = Preferencia?.edit()
        Editor?.putString(EMAIL, Email)
        Editor?.putString(SENHA, Senha)
        Editor?.putString(CIDADE, Cidade)
        Editor?.apply()
    }

    private fun recuperarPreferencias(): SharedPreferences? {
        return Contexto.getSharedPreferences(NOME_TABELA, 0)
    }

}