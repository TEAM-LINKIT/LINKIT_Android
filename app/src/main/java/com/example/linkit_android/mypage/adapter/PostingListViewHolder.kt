package com.example.linkit_android.mypage.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.linkit_android.R

class PostingListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var title: TextView = itemView.findViewById(R.id.item_title_posting)
    var writerDate: TextView = itemView.findViewById(R.id.item_writer_date_posting)

    fun onBind(data: PostingListData) {
        title.text = data.title
        writerDate.text = data.writerDate
    }
}