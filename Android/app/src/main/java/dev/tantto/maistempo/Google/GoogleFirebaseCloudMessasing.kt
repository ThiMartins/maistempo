package dev.tantto.maistempo.Google

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dev.tantto.maistempo.Notificacao.Notificacao


class GoogleFirebaseCloudMessasing : FirebaseMessagingService() {

    override fun onMessageReceived(p0: RemoteMessage?) {
        super.onMessageReceived(p0)

        Notificacao().NotificacaoCriar(this, "FirebaseRecebido", 10, "Mensagem recebida", p0?.notification?.title.toString(), "Firebase")

    }

}