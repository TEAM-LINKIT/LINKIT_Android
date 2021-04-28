package com.example.linkit_android.chatting.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.linkit_android.R

class ChatListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var profileImg: ImageView = itemView.findViewById(R.id.item_profile_img_chat)
    var name: TextView = itemView.findViewById(R.id.item_name_chat)
    var part: TextView = itemView.findViewById(R.id.item_part_chat)
    var content: TextView = itemView.findViewById(R.id.item_content_chat)

    fun onBind(data: ChatListData) {
        Glide.with(itemView).load(data.profileImg).into(profileImg)
        name.text = data.name
        part.text = data.part
        content.text = data.content
    }
}