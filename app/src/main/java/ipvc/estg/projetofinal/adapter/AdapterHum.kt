package ipvc.estg.projetofinal.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.projetofinal.CHumidade
import ipvc.estg.projetofinal.R
import kotlinx.android.synthetic.main.activity_recycler.view.*

// Usado para adicionar dados na nossa recyclerview
class AdapterHum(var list:ArrayList<CHumidade>) : RecyclerView.Adapter<AdapterHum.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var valor = itemView.valor
        var horas = itemView.horas
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.activity_recycler,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.valor.text=list[position].humidade.toString()
        holder.horas.text=list[position].horas
    }

    override fun getItemCount(): Int {
        return list.size
    }
}