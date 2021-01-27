package ipvc.estg.projetofinal.notification

data class PushNotification (
        //construtor dos parâmetros do envio da notificação
        val data: NotificationData,
        val to: String
        )