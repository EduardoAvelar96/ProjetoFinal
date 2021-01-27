package ipvc.estg.projetofinal

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.annotation.MenuRes
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.password
import kotlinx.android.synthetic.main.activity_login.username
import kotlinx.android.synthetic.main.activity_signup.*

var uid = ""

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

        button_register1.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
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
                    var user = auth.currentUser
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

        val user = auth.currentUser
        user?.let {
            for (profile in it.providerData) {
                // UID specific to the provider
                uid = profile.uid
            }
        }

        val sharedPref: SharedPreferences = getSharedPreferences(
                getString(R.string.preference_file_key),
                Context.MODE_PRIVATE
        )

        with ( sharedPref.edit() ) {
            putString(getString(R.string.id_login), uid)
            commit()
        }

        println(currentUser)
        if (currentUser != null) {
                if(valor == 1){
                    startActivity(Intent(this, Menuresp::class.java))
                    finish()
                }else if(valor == 2){
                    startActivity(Intent(this, Menu::class.java))
                    finish()
                }
        } else {
            Toast.makeText(
                baseContext, R.string.login_fail,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}