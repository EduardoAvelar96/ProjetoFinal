package ipvc.estg.projetofinal.notification

import ipvc.estg.projetofinal.notification.Constants.Companion.CONTENT_TYPE
import ipvc.estg.projetofinal.notification.Constants.Companion.SERVER_KEY
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationAPI {
    //Comunicação com a API enviando um pedido HTTP POST para o url fcm/send
    @Headers("Authorization: key=$SERVER_KEY","Content-Type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
            @Body notification: PushNotification
    ): Response<ResponseBody>
}