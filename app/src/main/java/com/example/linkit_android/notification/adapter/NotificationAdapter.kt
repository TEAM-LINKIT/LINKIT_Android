package com.example.linkit_android.notification.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.linkit_android.R

class NotificationAdapter(private val context: Context) : RecyclerView.Adapter<NotificationViewHolder>() {

    var data = mutableListOf<NotificationData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder  {
        val view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.onBind(data[position])
    }

    override fun getItemCount(): Int = data.size

}