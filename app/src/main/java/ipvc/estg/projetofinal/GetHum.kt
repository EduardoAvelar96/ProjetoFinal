package ipvc.estg.projetofinal

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import ipvc.estg.projetofinal.adapter.AdapterHum
import kotlinx.android.synthetic.main.activity_getdata.*

class GetHum : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_getdata)


        database = FirebaseDatabase.getInstance()
        reference = database.getReference("Humidade")

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this)

        getData()
    }

    private fun getData(){

        //Chama o sharedPref
        val sharedPref: SharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE )
        val id = sharedPref.getString(getString(R.string.id_login), "")

        reference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var list = ArrayList<CHumidade>()
                for(data in snapshot.children){
                    var model = data.getValue((CHumidade::class.java))
                    if(model!!.id == id) {
                        list.add(model as CHumidade)
                    }
                }
                if(list.size > 0){
                    var adapter = AdapterHum(list)
                    recyclerview.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("cancel", error.toString())
            }

        })
    }
}