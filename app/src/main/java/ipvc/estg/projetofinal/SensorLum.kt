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

class SensorLum : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var luz: Sensor? = null
    var isRunning = false
    //teste
    var ultimoVal = -500

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lum)

        // Get an instance of the sensor service, and use that to get an instance of
        // a particular sensor.
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        luz = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Do something here if sensor accuracy changes.
    }

    override fun onSensorChanged(event: SensorEvent) {
        val luz = event.values[0].toInt()

        findViewById<TextView>(R.id.luminosidade1).setText(R.string.lum_igual)
        findViewById<TextView>(R.id.luminosidade2).setText(luz.toString())
        findViewById<TextView>(R.id.luminosidade3).setText("lx")

        try {
            if (luz < 300 && !isRunning) {
                if(ultimoVal != luz){
                    saveLum(luz)
                }
                ultimoVal = luz
                isRunning = true
                startActivity(Intent(this, Alerta::class.java))
            }else{
                isRunning = false
            }
        } catch (e: Exception) {

        }
    }

    override fun onResume() {
        // Register a listener for the sensor.
        super.onResume()
        sensorManager.registerListener(this, luz, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    private fun saveLum(luz: Int) {

        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())

        val ref = FirebaseDatabase.getInstance().getReference("Luminosidade")

        val luminosidadeID = ref.push().key
        val luminosidade = CLuminosidade(luz,currentDate)

        ref.child(luminosidadeID!!).setValue(luminosidade).addOnCompleteListener{
            Toast.makeText(applicationContext, R.string.lum_salva, Toast.LENGTH_LONG).show()
        }
    }
}
