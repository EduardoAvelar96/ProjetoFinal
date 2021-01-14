package ipvc.estg.projetofinal

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import ipvc.estg.projetofinal.adapter.AdapterTemp
import ipvc.estg.projetofinal.notification.NotificationData
import ipvc.estg.projetofinal.notification.PushNotification
import ipvc.estg.projetofinal.notification.RetrofitInstance
import kotlinx.android.synthetic.main.activity_getdata.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GetTemp : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_getdata)

        database = FirebaseDatabase.getInstance()
        reference = database.getReference("Temperatura")

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this)

        getData()

    }

    private fun getData(){
        reference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var list = ArrayList<CTemperatura>()
                for(data in snapshot.children){
                    var model = data.getValue((CTemperatura::class.java))
                    list.add(model as CTemperatura)
                }
                if(list.size > 0){
                    var adapter = AdapterTemp(list)
                    recyclerview.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("cancel", error.toString())
            }

        })
    }
}