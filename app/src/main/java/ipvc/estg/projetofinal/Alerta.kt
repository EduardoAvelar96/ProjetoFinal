package ipvc.estg.projetofinal

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Alerta : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alert)

        val button = findViewById<Button>(R.id.button)

        button.setOnClickListener{
            finish()
        }
    }
}