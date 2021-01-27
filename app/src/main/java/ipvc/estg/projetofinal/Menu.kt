package ipvc.estg.projetofinal

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.toHalf
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import ipvc.estg.projetofinal.notification.FirebaseService
import ipvc.estg.projetofinal.notification.NotificationData
import ipvc.estg.projetofinal.notification.PushNotification
import ipvc.estg.projetofinal.notification.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

const val TOPIC = "/topics/myTopic" //.plus(uid.subSequence(0,5))

class Menu : AppCompatActivity(), SensorEventListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var sensorManager: SensorManager
    private var humidity: Sensor? = null
    private var luz: Sensor? = null
    private var temperature: Sensor? = null
    var isRunning = false

    //var de notificaçoes
    val TAG = "Menu"
    val title = "ALERTA"
    val message = "Condições atmosféricas não favoráveis"

    var ultimoVal = -500

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)


        auth = FirebaseAuth.getInstance()
        //FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        humidity = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)
        luz = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        temperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)


        val button4 = findViewById<Button>(R.id.button4)
        val button5 = findViewById<Button>(R.id.button5)
        val button6 = findViewById<Button>(R.id.button6)
        button4.setOnClickListener {
            startActivity(Intent(this, GetTemp::class.java))
        }
        button5.setOnClickListener {
            startActivity(Intent(this, GetLum::class.java))
        }
        button6.setOnClickListener {
            startActivity(Intent(this, GetHum::class.java))
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Do something here if sensor accuracy changes.
    }

    override fun onSensorChanged(event: SensorEvent) {

        when (event.sensor.type) {

            Sensor.TYPE_RELATIVE_HUMIDITY -> {
                val humidity = event.values[0].toInt()
                val string = getString(R.string.hum_igual)
                val medida = "%"

                val string1 = "$string $humidity $medida"

                findViewById<TextView>(R.id.humidade).setText(string1)

                try {
                    if (humidity < 30 || humidity > 70 && !isRunning) {
                        if (ultimoVal != humidity) {
                            ultimoVal = humidity
                            saveHum(humidity)
                            startActivity(Intent(this, Alerta::class.java))
                            isRunning = true
                            PushNotification(
                                    NotificationData(title, message),
                                    TOPIC
                            ).also{
                                sendNotification(it)
                            }
                        }
                    } else {
                        isRunning = false
                    }
                } catch (e: Exception) {

                }
            }
            Sensor.TYPE_AMBIENT_TEMPERATURE -> {
                val temp = event.values[0].toInt()
                val string = getString(R.string.temp_igual)
                val medida = "°C"

                val string1 = "$string $temp $medida"

                findViewById<TextView>(R.id.temperatura).setText(string1)

                try {
                    if (temp < 0 || temp > 30 && !isRunning) {
                        if (ultimoVal != temp) {
                            ultimoVal = temp
                            saveTemp(temp)
                            startActivity(Intent(this, Alerta::class.java))
                            isRunning = true
                            PushNotification(
                                    NotificationData(title, message),
                                    TOPIC
                            ).also{
                                sendNotification(it)
                            }
                        }
                    } else {
                        isRunning = false
                    }
                } catch (e: Exception) {

                }
            }
            Sensor.TYPE_LIGHT -> {
                val luz = event.values[0].toInt()
                val string = getString(R.string.lum_igual)
                val medida = "lx"

                val string1 = "$string $luz $medida"

                findViewById<TextView>(R.id.luminosidade).setText(string1)

                try {
                    if (luz < 300 && !isRunning) {
                        if (ultimoVal != luz) {
                            ultimoVal = luz
                            saveLum(luz)
                            startActivity(Intent(this, Alerta::class.java))
                            isRunning = true
                            PushNotification(
                                    NotificationData(title, message),
                                    TOPIC
                            ).also{
                                sendNotification(it)
                            }
                        }
                    } else {
                        isRunning = false
                    }
                } catch (e: Exception) {

                }
            }
        }
    }

    override fun onResume() {
        // Register a listener for the sensor.
        super.onResume()
        sensorManager.registerListener(this, this.humidity, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, this.temperature, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, this.luz, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause()
        //PARA FUNCIONAR EM BACKGROUND BASTA REMOVER ESTA LINHA
        sensorManager.unregisterListener(this)
    }

    private fun saveHum(humidity: Int) {

        //Chama o sharedPref
        val sharedPref: SharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE )
        val id = sharedPref.getString(getString(R.string.id_login), "")

        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())

        val ref = FirebaseDatabase.getInstance().getReference("Humidade")

        //id que corresponde á ref
        val humidadeID = ref.push().key

        val humidade = CHumidade(humidity, currentDate,id!!)


        ref.child(humidadeID!!).setValue(humidade).addOnCompleteListener {
            Toast.makeText(applicationContext, R.string.hum_salva, Toast.LENGTH_LONG).show()
        }
    }

    private fun saveLum(luz: Int) {

        //Chama o sharedPref
        val sharedPref: SharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE )
        val id = sharedPref.getString(getString(R.string.id_login), "")

        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())

        val ref = FirebaseDatabase.getInstance().getReference("Luminosidade")

        val luminosidadeID = ref.push().key
        val luminosidade = CLuminosidade(luz, currentDate,id!!)

        ref.child(luminosidadeID!!).setValue(luminosidade).addOnCompleteListener {
            Toast.makeText(applicationContext, R.string.lum_salva, Toast.LENGTH_LONG).show()
        }
    }

    private fun saveTemp(temp: Int) {

        //Chama o sharedPref
        val sharedPref: SharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE )
        val id = sharedPref.getString(getString(R.string.id_login), "")

        var ref = FirebaseDatabase.getInstance().getReference("Temperatura")

        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())

        val temperaturaID = ref.push().key
        val temperatura = CTemperatura(temp, currentDate,id!!)

        ref.child(temperaturaID!!).setValue(temperatura).addOnCompleteListener {
            Toast.makeText(applicationContext, R.string.temp_salva, Toast.LENGTH_LONG).show()
        }
    }

    //Ver menu opções
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    //Menu de opções
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.opt1 -> {
                auth.signOut()
                startActivity(Intent(this, Login::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch{
        try{
            //Ocorre a request da notificação/ chama a interface notificationAPI
            val response = RetrofitInstance.api!!.postNotification(notification)
            if(response.isSuccessful){
                Log.d(TAG, "Response: ${Gson().toJson(response)}")
            }else{
                Log.e(TAG, response.errorBody().toString())
            }
        }catch (e: java.lang.Exception){
            Log.e(TAG, e.toString())
        }
    }
}