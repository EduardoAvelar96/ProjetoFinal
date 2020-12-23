package ipvc.estg.projetofinal

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView

class SensorHum : Activity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var humidity: Sensor? = null
    var isRunning = false

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensorhum)

        // Get an instance of the sensor service, and use that to get an instance of
        // a particular sensor.
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        humidity = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Do something here if sensor accuracy changes.
    }

    override fun onSensorChanged(event: SensorEvent) {
        val humidity = event.values[0]
        try {
            if (humidity < 50 && !isRunning) {
                isRunning = true
                findViewById<TextView>(R.id.HumNega).setText("Humidade: " + humidity + "%")
                findViewById<TextView>(R.id.HumPosi).setText("")

            } else if(humidity > 50 && !isRunning){
                findViewById<TextView>(R.id.HumNega).setText("")
                findViewById<TextView>(R.id.HumPosi).setText("Humidade: " + humidity + "%")

            }else{
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
}
