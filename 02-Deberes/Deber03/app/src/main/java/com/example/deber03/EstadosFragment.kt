package com.example.deber03

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class EstadosFragment : Fragment() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_estados, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewEstados)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val estadosList = listOf(
            Estado("Alice", "Hace 10 minutos", R.drawable.profile_image, R.drawable.estado_imagen1),
            Estado("Bob", "Hace 1 hora", R.drawable.profile_image, R.drawable.estado_imagen2),
            Estado("Charlie", "Ayer", R.drawable.profile_image, R.drawable.estado_imagen3)
        )

        recyclerView.adapter = EstadoAdapter(estadosList) { estado ->
            // Manejar clic en estado
        }

        return view
    }
}