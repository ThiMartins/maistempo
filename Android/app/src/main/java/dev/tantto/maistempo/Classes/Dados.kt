package dev.tantto.maistempo.classes

import android.content.Context

class Dados {

    companion object {

        fun salvarLogin(Modo:Boolean, Contexto:Context){
            val Preferencias = Contexto.getSharedPreferences("+Tempo", Context.MODE_PRIVATE)
            val Editor = Preferencias.edit()
            Editor.putBoolean("Login", Modo)
            Editor.apply()
        }

        fun recuperarLogin(Contexto: Context) : Boolean {
            return Contexto.getSharedPreferences("+Tempo", Context.MODE_PRIVATE).getBoolean("Login", false)
        }

        fun salvarLoja(Contexto: Context, Id:String, Horario:String){
            val Preferencias = Contexto.getSharedPreferences("+Tempo", Context.MODE_PRIVATE)
            val Editor = Preferencias.edit()
            Editor.putString(Id, Horario)
            Editor.apply()
        }

        fun verificarLojaHorario(Contexto: Context, Id: String) : String? {
            return Contexto.getSharedPreferences("+Tempo", Context.MODE_PRIVATE).getString(Id, "")
        }

        fun salvarLocal(Contexto: Context, Local:String) {
            val Preferencias = Contexto.getSharedPreferences("+Tempo", Context.MODE_PRIVATE)
            val Editor = Preferencias.edit()
            Editor.putString("Local", Local)
            Editor.apply()
        }

        fun verificarLocal(Contexto: Context) : String? {
            return Contexto.getSharedPreferences("+Tempo", Context.MODE_PRIVATE).getString("Local", "")
        }

        fun salvarAutorizacao(Contexto: Context, Modo: Boolean) {
            val Preferencias = Contexto.getSharedPreferences("+Tempo", Context.MODE_PRIVATE)
            val Editor = Preferencias.edit()
            Editor.putBoolean("ADM", Modo)
            Editor.apply()
        }

        fun verificarAutorizacao(Contexto: Context) : Boolean {
            return Contexto.getSharedPreferences("+Tempo", Context.MODE_PRIVATE).getBoolean("ADM", false)
        }


    }


}