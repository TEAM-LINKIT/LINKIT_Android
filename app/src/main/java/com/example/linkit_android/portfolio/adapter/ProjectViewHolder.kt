package com.example.linkit_android.portfolio.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.linkit_android.R

class ProjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var projectImg: ImageView = itemView.findViewById(R.id.item_project_img)
    var title: TextView = itemView.findViewById(R.id.item_title)
    var content: TextView = itemView.findViewById(R.id.item_content)
    var link: ImageView = itemView.findViewById(R.id.btn_link)

    fun onBind(data: ProjectData) {
        Glide.with(itemView).load(data.projectImg).into(projectImg)
        title.text = data.title
        content.text = data.content
    }
}