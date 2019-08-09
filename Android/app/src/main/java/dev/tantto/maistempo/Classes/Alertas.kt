package dev.tantto.maistempo.classes

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
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

        fun criarAlertDialog(Contexto:Context, Mensagem: Int, Titulo: Int) : AlertDialog.Builder {
            val Alerta = AlertDialog.Builder(Contexto)
            Alerta.setTitle(Titulo)
            Alerta.setMessage(Mensagem)
            return Alerta
        }

        @SuppressLint("InflateParams")
        fun criarAlertDialog(Contexto:Context) : AlertDialog.Builder {
            val Alerta = AlertDialog.Builder(Contexto)
            val ViewInflado = LayoutInflater.from(Contexto).inflate(R.layout.calendario_alternativo, null)
            Alerta.setView(ViewInflado)
            Alerta.setTitle(R.string.DataNascimento)
            return Alerta
        }

        fun alertaCarregando(Contexto:Context) : AlertDialog{
            val AlertaBuilder = AlertDialog.Builder(Contexto)
            AlertaBuilder.setView(R.layout.loading)
            val Alerta = AlertaBuilder.create()
            Alerta.window?.setBackgroundDrawableResource(R.color.Transparente)
            return Alerta
        }
    }

}