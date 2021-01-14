package ipvc.estg.projetofinal

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import ipvc.estg.projetofinal.adapter.AdapterLum
import kotlinx.android.synthetic.main.activity_getdata.*

class GetLum : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_getdata)

        database = FirebaseDatabase.getInstance()
        reference = database.getReference("Luminosidade")

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this)

        getData()

    }

    private fun getData(){
        reference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var list = ArrayList<CLuminosidade>()
                for(data in snapshot.children){
                    var model = data.getValue((CLuminosidade::class.java))
                    list.add(model as CLuminosidade)
                }
                if(list.size > 0){
                    var adapter = AdapterLum(list)
                    recyclerview.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("cancel", error.toString())
            }

        })
    }

}