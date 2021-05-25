package com.example.linkit_android.notification.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.linkit_android.R
import com.example.linkit_android.util.ItemClickListener
import kotlinx.android.synthetic.main.item_notification.view.*

class NotificationAdapter(private val context: Context) : RecyclerView.Adapter<NotificationViewHolder>() {

    var data = mutableListOf<NotificationData>()
    private lateinit var itemClickListener: ItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder  {
        val view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.apply {
            onBind(data[position])
            itemView.item_constraint_notification.setOnClickListener {
                itemClickListener.onClickItem(it, position)
            }
        }
        if (data[position].read)
            holder.itemView.view_alert_portfolio.visibility = View.INVISIBLE
        else
            holder.itemView.view_alert_portfolio.visibility = View.VISIBLE
    }

    override fun getItemCount(): Int = data.size

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

}