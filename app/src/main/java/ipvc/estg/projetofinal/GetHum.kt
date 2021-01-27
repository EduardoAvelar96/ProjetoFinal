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

    //Referencia á Firebase/BD
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_getdata)

        //Obtem a instancia da base dados e a respetiva referencia
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("Humidade")

        //Recycler no xml e defenir um layoutmanager
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this)

        getData()
    }

    private fun getData(){

        //Chama o sharedPref
        val sharedPref: SharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE )
        val id = sharedPref.getString(getString(R.string.id_login), "")

        //listener para o campo que faz referencia á humidade na bd
        reference.addValueEventListener(object: ValueEventListener {
            //sempre que acrescentarem dados ou removerem
            override fun onDataChange(snapshot: DataSnapshot) {
                var list = ArrayList<CHumidade>()
                //buscar todos os valores á base de dados
                for(data in snapshot.children){
                    //guarda numa variável model os valores que estão no campo humidade
                    var model = data.getValue((CHumidade::class.java))
                    //caso o campo id corresponda ao id que vem da sharedpref adiciona á lista
                    if(model!!.id == id) {
                        list.add(model as CHumidade)
                    }
                }
                if(list.size > 0){
                    //Caso tenha valores inserios no recycler view
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