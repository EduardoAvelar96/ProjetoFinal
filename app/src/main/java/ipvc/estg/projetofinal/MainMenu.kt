package ipvc.estg.projetofinal

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class MainMenu : AppCompatActivity(){

    private lateinit var auth: FirebaseAuth

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        auth = FirebaseAuth.getInstance()

        val button1 = findViewById<Button>(R.id.button1)
        val button2 = findViewById<Button>(R.id.button2)
        val button3 = findViewById<Button>(R.id.button3)
        val button4 = findViewById<Button>(R.id.button4)
        val button5 = findViewById<Button>(R.id.button5)
        val button6 = findViewById<Button>(R.id.button6)
        button1.setOnClickListener {
            startActivity(Intent(this, SensorTemp::class.java))
        }
        button2.setOnClickListener {
            startActivity(Intent(this, SensorLum::class.java))
        }
        button3.setOnClickListener {
            startActivity(Intent(this, SensorHum::class.java))
        }
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

    //Ver menu opções
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    //Menu de opções
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.opt1->{
                auth.signOut()
                startActivity(Intent(this, Login::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
