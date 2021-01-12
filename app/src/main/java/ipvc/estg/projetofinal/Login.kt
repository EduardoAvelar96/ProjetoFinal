package ipvc.estg.projetofinal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.annotation.MenuRes
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {

    var valor = 0

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        button_login.setOnClickListener{
            valor = 1
            doLogin(valor)
        }

        button_login2.setOnClickListener {
            valor = 2
            doLogin(valor)
        }
    }

    private fun doLogin(valor: Int) {
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
                    updateUI(user,valor)
                } else {
                    updateUI(null,valor)
                }
            }
    }



    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser,valor)
    }

    private fun updateUI(currentUser: FirebaseUser?,valor : Int) {
        if (currentUser != null) {
            if(currentUser.isEmailVerified) {
                if(valor == 1){
                    startActivity(Intent(this, Menuresp::class.java))
                    finish()
                }else if(valor == 2){
                    startActivity(Intent(this, Menu::class.java))
                    finish()
                }
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