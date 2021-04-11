package com.example.linkit_android.community.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.linkit_android.R

class CommunityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var title: TextView = itemView.findViewById(R.id.item_title)
    var partList: TextView = itemView.findViewById(R.id.item_part)

    fun onBind(data: CommunityData) {
        title.text = data.title
        partList.text = data.partList
    }
}