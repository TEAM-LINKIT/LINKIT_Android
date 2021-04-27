package com.example.linkit_android.portfolio.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.linkit_android.R

class FillRedTagAdapter(private val context: Context) : RecyclerView.Adapter<FillRedTagViewHolder>() {

    var data = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FillRedTagViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_tag_fill_red, parent, false)
        return FillRedTagViewHolder(view)
    }

    override fun onBindViewHolder(holder: FillRedTagViewHolder, position: Int) {
        holder.onBind(data[position])
    }

    override fun getItemCount(): Int = data.size
}