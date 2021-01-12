package ipvc.estg.projetofinal

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class Menu : AppCompatActivity(), SensorEventListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var sensorManager: SensorManager
    private var all: Sensor? = null
    private var humidity: Sensor? = null
    private var luz: Sensor? = null
    private var temperature: Sensor? = null
    var isRunning = false

    var ultimoVal = -500

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        auth = FirebaseAuth.getInstance()


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
                            saveHum(humidity)
                            startActivity(Intent(this, Alerta::class.java))
                            isRunning = true
                        }
                        ultimoVal = humidity
                        //isRunning = true
                        //startActivity(Intent(this, Alerta::class.java))
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
                            saveTemp(temp)
                            startActivity(Intent(this, Alerta::class.java))
                            isRunning = true
                        }
                        ultimoVal = temp
                        //isRunning = true
                        //startActivity(Intent(this, Alerta::class.java))
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
                            saveLum(luz)
                            startActivity(Intent(this, Alerta::class.java))
                            isRunning = true
                        }
                        ultimoVal = luz
                        //isRunning = true
                        //startActivity(Intent(this, Alerta::class.java))
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


    }

    private fun saveHum(humidity: Int) {

        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())

        val ref = FirebaseDatabase.getInstance().getReference("Humidade")

        val humidadeID = ref.push().key
        val humidade = CHumidade(humidity, currentDate)

        ref.child(humidadeID!!).setValue(humidade).addOnCompleteListener {
            Toast.makeText(applicationContext, R.string.hum_salva, Toast.LENGTH_LONG).show()
        }
    }

    private fun saveLum(luz: Int) {

        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())

        val ref = FirebaseDatabase.getInstance().getReference("Luminosidade")

        val luminosidadeID = ref.push().key
        val luminosidade = CLuminosidade(luz, currentDate)

        ref.child(luminosidadeID!!).setValue(luminosidade).addOnCompleteListener {
            Toast.makeText(applicationContext, R.string.lum_salva, Toast.LENGTH_LONG).show()
        }
    }

    private fun saveTemp(temp: Int) {

        var ref = FirebaseDatabase.getInstance().getReference("Temperatura")

        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())

        val temperaturaID = ref.push().key
        val temperatura = CTemperatura(temp, currentDate)

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
}