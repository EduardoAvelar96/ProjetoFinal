package ipvc.estg.projetofinal

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView

class SensorLum : Activity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var luz: Sensor? = null
    var isRunning = false

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensorlum)

        // Get an instance of the sensor service, and use that to get an instance of
        // a particular sensor.
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        luz = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Do something here if sensor accuracy changes.
    }

    override fun onSensorChanged(event: SensorEvent) {
        val luz = event.values[0]
        try {
            if (luz < 20000 && !isRunning) {
                isRunning = true
                findViewById<TextView>(R.id.LuzNega).setText("Luz: " + luz + "lx")
                findViewById<TextView>(R.id.LuzPosi).setText("")

            } else if(luz > 20000 && !isRunning){
                findViewById<TextView>(R.id.LuzNega).setText("")
                findViewById<TextView>(R.id.LuzPosi).setText("Luz: " + luz + "lx")

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
}
