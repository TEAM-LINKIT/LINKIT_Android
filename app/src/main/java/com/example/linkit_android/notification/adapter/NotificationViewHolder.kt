package com.example.linkit_android.notification.adapter

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.linkit_android.R

class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var profileImg: ImageView = itemView.findViewById(R.id.item_profile_img_notification)
    var name: TextView = itemView.findViewById(R.id.item_name_notification)
    var part: TextView = itemView.findViewById(R.id.item_part_notification)
    var title: TextView = itemView.findViewById(R.id.item_title_notification)

    fun onBind(data: NotificationData) {
        Glide.with(itemView).load(data.profileImg).into(profileImg)
        name.text = data.name
        part.text = data.part
        title.text = data.name + "님이 " + data.title + " 글에 포트폴리오를 전송했습니다."
    }
}