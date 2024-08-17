package com.example.deber03


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EstadoAdapter(private val estadosList: List<Estado>, private val onItemClick: (Estado) -> Unit) :
    RecyclerView.Adapter<EstadoAdapter.EstadoViewHolder>() {

    class EstadoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagenPerfil: ImageView = itemView.findViewById(R.id.imagenPerfil)
        val nombreEstado: TextView = itemView.findViewById(R.id.nombreEstado)
        val horaEstado: TextView = itemView.findViewById(R.id.horaEstado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EstadoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_estado, parent, false)
        return EstadoViewHolder(view)
    }

    override fun onBindViewHolder(holder: EstadoViewHolder, position: Int) {
        val estado = estadosList[position]
        holder.imagenPerfil.setImageResource(estado.imagenPerfil)
        holder.nombreEstado.text = estado.nombre
        holder.horaEstado.text = estado.hora

        if (estado.imagenEstado != 0) {
            holder.imagenPerfil.background = holder.itemView.context.getDrawable(R.drawable.borde_estado)
        } else {
            holder.imagenPerfil.background = null
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, VerEstadoActivity::class.java)
            intent.putExtra("imagenEstado", estado.imagenEstado)
            intent.putExtra("nombreUsuario", estado.nombre)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = estadosList.size
}