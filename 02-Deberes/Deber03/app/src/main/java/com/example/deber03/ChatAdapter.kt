package com.example.deber03

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter(private val chatsList: List<Chat>, private val onItemClick: (Chat) -> Unit) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagenPerfilChat: ImageView = itemView.findViewById(R.id.imagenPerfilChat)
        val nombreChat: TextView = itemView.findViewById(R.id.nombreChat)
        val mensajeChat: TextView = itemView.findViewById(R.id.mensajeChat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = chatsList[position]
        holder.imagenPerfilChat.setImageResource(chat.imagenPerfil)
        holder.nombreChat.text = chat.nombre
        holder.mensajeChat.text = chat.mensaje

        holder.itemView.setOnClickListener {
            onItemClick(chat)
        }
    }

    override fun getItemCount() = chatsList.size
}