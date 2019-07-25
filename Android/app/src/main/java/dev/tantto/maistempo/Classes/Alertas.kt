package dev.tantto.maistempo.Classes

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import com.tapadoo.alerter.Alerter
import dev.tantto.maistempo.R

class Alertas {

    companion object {

        fun criarAlerter(Tela: Activity, Mensagem:Int, Titulo:Int, Duracao:Long = 10000) : Alerter{
            val Alerta = Alerter.create(Tela)
            Alerta.setText(Mensagem)
            Alerta.setTitle(Titulo)
            Alerta.setBackgroundColor(R.color.colorPrimary)
            Alerta.setDuration(Duracao)
            return  Alerta
        }

        fun criarAlerter(Tela: Activity, Mensagem:String, Titulo:Int, Duracao:Long = 10000) : Alerter{
            val Alerta = Alerter.create(Tela)
            Alerta.setText(Mensagem)
            Alerta.setTitle(Titulo)
            Alerta.setBackgroundColor(R.color.colorPrimary)
            Alerta.setDuration(Duracao)
            return  Alerta
        }

        fun CriarAlertDialog(Contexto:Context, Mensagem: Int, Titulo: Int) : AlertDialog.Builder {
            val Alerta = AlertDialog.Builder(Contexto)
            Alerta.setTitle(Titulo)
            Alerta.setMessage(Mensagem)
            return Alerta
        }

    }

}