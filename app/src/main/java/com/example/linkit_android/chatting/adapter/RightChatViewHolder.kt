package com.example.linkit_android.chatting.adapter

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.linkit_android.R
import com.example.linkit_android.databinding.ItemChatRightContentBinding

class RightChatViewHolder(var binding: ItemChatRightContentBinding) : RecyclerView.ViewHolder(binding.root) {

    var content: TextView = itemView.findViewById(R.id.item_content_right)

    fun onBind(data: ChatData) {
        content.text = data.content
    }
}