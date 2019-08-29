package dev.tantto.maistempo.classes

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dev.tantto.maistempo.R
import dev.tantto.maistempo.google.CloudFunctions
import dev.tantto.maistempo.notificacao.Notificacao
import java.util.*


class Alarme : BroadcastReceiver (){

    companion object {

        fun desabilitarAlarme(Contexto:Context){
            val Intencao = Intent(Contexto, Alarme::class.java)
            val PendingIntencao = PendingIntent.getBroadcast(Contexto, 0, Intencao, 0)
            val Gerenciador = Contexto.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            Gerenciador.cancel(PendingIntencao)
        }

        fun habilitarAlarme(Contexto: Context){
            val Intencao = Intent(Contexto, Alarme::class.java)
            val PendingIntencao = PendingIntent.getBroadcast(Contexto, 0, Intencao, 0)
            val Gerenciador = Contexto.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            val Calendario = Calendar.getInstance()
            Calendario.timeInMillis = System.currentTimeMillis()
            Calendario.set(Calendar.HOUR_OF_DAY, 1)
            val Inicio = Calendario.timeInMillis

            Gerenciador.setRepeating(AlarmManager.RTC_WAKEUP, Inicio, AlarmManager.INTERVAL_DAY, PendingIntencao)
        }

    }

    override fun onReceive(p0: Context?, p1: Intent?) {
        if(p0 != null){
            Notificacao().notificacaoCriar(p0, "15", 15, p0.getString(R.string.BancoMudanca), p0.getString(R.string.AtualizandoBanco), p0.getString(R.string.AtualizandoBanco))
            CloudFunctions.atualizarLista(p0)
        }
    }
}