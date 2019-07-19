package dev.tantto.maistempo.Notificacao

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import dev.tantto.maistempo.R
import dev.tantto.maistempo.Telas.TelaPrincipal

class Notificacao {

    fun NotificacaoCriar(Contexto:Context, Id:String, Codigo:Int, Titulo:String, Corpo:String, Sumario:String){
        val Alerta = NotificationCompat.Builder(Contexto, Id)
        val Intencao = Intent(Contexto, TelaPrincipal::class.java)
        val IntencaoNotificacao = PendingIntent.getActivities(Contexto, Codigo, arrayOf(Intencao), 0)

        val TextoGrande = NotificationCompat.BigTextStyle()
        TextoGrande.bigText(Titulo)
        TextoGrande.setBigContentTitle(Corpo)
        TextoGrande.setSummaryText(Sumario)

        Alerta.setContentIntent(IntencaoNotificacao)
        Alerta.setSmallIcon(R.drawable.ic_launcher_foreground)
        Alerta.setContentTitle(Titulo)
        Alerta.setContentText(Corpo)
        Alerta.priority = Notification.PRIORITY_MAX
        Alerta.setStyle(TextoGrande)

        val Gerenciador = Contexto.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= 26){
            val Canal = NotificationChannel(Id, Id, NotificationManager.IMPORTANCE_DEFAULT)
            Gerenciador.createNotificationChannel(Canal)
            Alerta.setChannelId(Id)
        }

        Gerenciador.notify(0, Alerta.build())

    }

}