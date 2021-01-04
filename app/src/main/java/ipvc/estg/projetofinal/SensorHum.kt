package ipvc.estg.projetofinal

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class SensorHum : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var humidity: Sensor? = null
    var isRunning = false

    var ultimoVal = -500

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hum)

        // Get an instance of the sensor service, and use that to get an instance of
        // a particular sensor.
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        humidity = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Do something here if sensor accuracy changes.
    }

    override fun onSensorChanged(event: SensorEvent) {

        val humidity = event.values[0].toInt()

        findViewById<TextView>(R.id.humidade1).setText(R.string.hum_igual)
        findViewById<TextView>(R.id.humidade2).setText(humidity.toString())
        findViewById<TextView>(R.id.humidade3).setText("%")

        try {
            if (humidity < 30 || humidity > 70 && !isRunning) {
                if(ultimoVal != humidity){
                    saveHum(humidity)
                }
                ultimoVal = humidity
                isRunning = true
                startActivity(Intent(this, Alerta::class.java))
            } else{
                isRunning = false
            }
        } catch (e: Exception) {

        }
    }

    override fun onResume() {
        // Register a listener for the sensor.
        super.onResume()
        sensorManager.registerListener(this, humidity, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    private fun saveHum(humidity: Int) {

        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())

        val ref = FirebaseDatabase.getInstance().getReference("Humidade")

        val humidadeID = ref.push().key
        val humidade = CHumidade(humidity,currentDate)

        ref.child(humidadeID!!).setValue(humidade).addOnCompleteListener{
            Toast.makeText(applicationContext, R.string.temp_salva, Toast.LENGTH_LONG).show()
        }
    }
}
