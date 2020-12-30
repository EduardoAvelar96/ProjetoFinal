package ipvc.estg.projetofinal

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SensorTemp : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var temperature: Sensor? = null
    var isRunning = false

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temp)

        // Get an instance of the sensor service, and use that to get an instance of
        // a particular sensor.
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        temperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Do something here if sensor accuracy changes.
    }

    override fun onSensorChanged(event: SensorEvent) {
        val temp = event.values[0]

        findViewById<TextView>(R.id.temperatura1).setText(R.string.temp_igual)
        findViewById<TextView>(R.id.temperatura2).setText(temp.toString())
        findViewById<TextView>(R.id.temperatura3).setText("°C")

        try {
            if (temp < 0 && !isRunning) {
                isRunning = true

            } else if(temp > 0 && !isRunning){

            }else{
                isRunning = false
            }
        } catch (e: Exception) {

        }
    }

    override fun onResume() {
        // Register a listener for the sensor.
        super.onResume()
        sensorManager.registerListener(this, temperature, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}
