package ipvc.estg.projetofinal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        button_login.setOnClickListener {
            doLogin()
        }
    }

    private fun doLogin() {
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

        auth.signInWithEmailAndPassword(username.text.toString(), password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    updateUI(null)
                }
            }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            if(currentUser.isEmailVerified) {
                startActivity(Intent(this, Menu::class.java))
                finish()
            }else{
                Toast.makeText(
                    baseContext, R.string.email_ver,
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(
                baseContext, R.string.login_fail,
                Toast.LENGTH_SHORT
            ).show()
        }
    }


}