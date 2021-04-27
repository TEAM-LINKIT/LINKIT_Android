package com.example.linkit_android.portfolio.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.linkit_android.R

class StrokeRedTagViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var tagText: TextView = itemView.findViewById(R.id.item_tag_stroke)

    fun onBind(data: String) {
        tagText.text = data
    }
}