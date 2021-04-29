package com.example.linkit_android.mypage.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.linkit_android.R

class PostingListAdapter(private val context: Context) : RecyclerView.Adapter<PostingListViewHolder>() {

    var data = mutableListOf<PostingListData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostingListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_posting_list, parent, false)
        return PostingListViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostingListViewHolder, position: Int) {
        holder.onBind(data[position])
    }

    override fun getItemCount(): Int = data.size
}