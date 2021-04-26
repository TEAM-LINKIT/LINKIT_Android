package com.example.linkit_android.portfolio.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.linkit_android.R

class StrokeRedTagAdapter(private val context: Context) : RecyclerView.Adapter<StrokeRedTagViewHolder>() {

    var data = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StrokeRedTagViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_tag_stroke_red, parent, false)
        return StrokeRedTagViewHolder(view)
    }

    override fun onBindViewHolder(holder: StrokeRedTagViewHolder, position: Int) {
        holder.onBind(data[position])
    }

    override fun getItemCount(): Int = data.size
}