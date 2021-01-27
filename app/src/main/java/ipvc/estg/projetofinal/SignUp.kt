package ipvc.estg.projetofinal

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_signup.*

class SignUp : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        auth = FirebaseAuth.getInstance()

        button_register.setOnClickListener {
            signUpUser()
        }
    }

    private fun signUpUser() {
        if (username.text.toString().isEmpty()) {
            username.error = getString(R.string.email)
            username.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(username.text.toString()).matches()) {
            username.error = getString(R.string.email_valid)
            username.requestFocus()
            return
        }

        if (password.text.toString().isEmpty()) {
            password.error = getString(R.string.password)
            password.requestFocus()
            return
        }

        auth.createUserWithEmailAndPassword(username.text.toString(), password.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        finish()
                    } else {
                        Toast.makeText(baseContext, R.string.register_error,
                                Toast.LENGTH_SHORT).show()
                    }
                }
    }
}