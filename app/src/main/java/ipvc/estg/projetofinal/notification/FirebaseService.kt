package ipvc.estg.projetofinal.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ipvc.estg.projetofinal.*
import kotlin.random.Random

private const val CHANNEL_ID = "my_channel"

//Serviço para receber as notificações
class FirebaseService: FirebaseMessagingService() {

    //Token gerado para cada sessão
    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        //Quando recebe a notificação para o tópico definido
        super.onMessageReceived(message)

        val intent = Intent(this, Alerta::class.java)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationID = Random.nextInt()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationchannel(notificationManager)
        }

        //Quando se clica na notificação o pendingIntent serve para não permitir a abertura de outra atividade com outro clique
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this,0,intent,FLAG_ONE_SHOT)
        //Builder da notificação em si
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(message.data["title"])
                .setContentText(message.data["message"])
                .setSmallIcon(R.drawable.ic_android_black_24dp)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()

        //Display da notificação
        notificationManager.notify(notificationID,notification)
    }

    //Criação do canal para as nossas notificações
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationchannel(notificationManager: NotificationManager){
        val channelName = "channelName"
        val channel = NotificationChannel(CHANNEL_ID,channelName,IMPORTANCE_HIGH).apply {
            description = "My channel description"
            enableLights(true)
            lightColor = Color.GREEN
        }
        notificationManager.createNotificationChannel(channel)
    }
}