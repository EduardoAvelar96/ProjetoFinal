package ipvc.estg.projetofinal.notification

import ipvc.estg.projetofinal.notification.Constants.Companion.BASE_URL
import kotlinx.android.synthetic.main.activity_recycler.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    companion object{
        private val retrofit by lazy{
            Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }
        val api by lazy {
            retrofit.create(NotificationAPI::class.java)
        }
    }
}