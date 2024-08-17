package com.example.deber03

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ChatsFragment : Fragment() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chats, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewChats)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val chatsList = listOf(
            Chat("Marco", "Pasame el código de Web", R.drawable.profile_image),
            Chat("Luis", "Mira el tiktok que te mande", R.drawable.profile_image),
            Chat("Nicol", "Que bonito perro que tienes :3", R.drawable.profile_image)
        )

        recyclerView.adapter = ChatAdapter(chatsList) { chat ->
            // Manejar clic en chat
        }

        return view
    }
}