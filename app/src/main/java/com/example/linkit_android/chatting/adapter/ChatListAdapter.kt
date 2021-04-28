package com.example.linkit_android.chatting.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.linkit_android.R

class ChatListAdapter(private val context: Context) : RecyclerView.Adapter<ChatListViewHolder>() {

    var data = mutableListOf<ChatListData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false)
        return ChatListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatListViewHolder, position: Int) {
        holder.onBind(data[position])
    }

    override fun getItemCount(): Int = data.size
}