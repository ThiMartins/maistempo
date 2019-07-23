package dev.tantto.maistempo.Dados

import android.content.Context
import android.content.SharedPreferences

class Dados(private val Contexto:Context) {

    companion object {
        private val NOME_TABELA = "ValoresTempo"
        private val EMAIL = "EMAIL"
        private val SENHA = "SENHA"
        private val CIDADE = "CIDADE"
        private val PERMISSAO = "PERMISSAO"
    }

    fun SalvarLogin(Email:String, Senha:String, Cidade:String){
        val Preferencia = RecuperarPreferencias()
        val Editor = Preferencia?.edit()
        Editor?.putString(EMAIL, Email)
        Editor?.putString(SENHA, Senha)
        Editor?.putString(CIDADE, Cidade)
        Editor?.apply()
    }

    private fun RecuperarPreferencias(): SharedPreferences? {
        return Contexto.getSharedPreferences(NOME_TABELA, 0)
    }

    fun Local() : String{
        val Preferencia = RecuperarPreferencias()
        return Preferencia?.getString(CIDADE, "")!!
    }

}