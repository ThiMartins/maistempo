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

    }


}