package dev.tantto.maistempo.Classes

import android.app.Activity
import com.tapadoo.alerter.Alerter
import dev.tantto.maistempo.R

class Alertas {

    companion object {

        fun CriarTela(Tela: Activity, Mensagem:Int, Titulo:Int, Duracao:Long = 10000) : Alerter{
            val Alerta = Alerter.create(Tela)
            Alerta.setText(Mensagem)
            Alerta.setTitle(Titulo)
            Alerta.setBackgroundColor(R.color.colorPrimary)
            Alerta.setDuration(Duracao)
            return  Alerta
        }

        fun CriarTela(Tela: Activity, Mensagem:String, Titulo:String, Duracao:Long = 10000) : Alerter{
            val Alerta = Alerter.create(Tela)
            Alerta.setText(Mensagem)
            Alerta.setTitle(Titulo)
            Alerta.setBackgroundColor(R.color.colorPrimary)
            Alerta.setDuration(Duracao)
            return  Alerta
        }

        fun CriarTela(Tela: Activity, Mensagem:String, Titulo:Int, Duracao:Long = 10000) : Alerter{
            val Alerta = Alerter.create(Tela)
            Alerta.setText(Mensagem)
            Alerta.setTitle(Titulo)
            Alerta.setBackgroundColor(R.color.colorPrimary)
            Alerta.setDuration(Duracao)
            return  Alerta
        }

    }

}