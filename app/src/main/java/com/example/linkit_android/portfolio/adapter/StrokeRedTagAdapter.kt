package com.example.linkit_android.portfolio.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.linkit_android.R
import com.example.linkit_android.util.ItemClickListener
import kotlinx.android.synthetic.main.item_tag_stroke_red.view.*

class StrokeRedTagAdapter(private val context: Context) : RecyclerView.Adapter<StrokeRedTagViewHolder>() {

    var data = mutableListOf<String>()
    private lateinit var itemClickListener: ItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StrokeRedTagViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_tag_stroke_red, parent, false)
        return StrokeRedTagViewHolder(view)
    }

    override fun onBindViewHolder(holder: StrokeRedTagViewHolder, position: Int) {
        holder.apply {
            onBind(data[position])
            itemView.item_tag_stroke.setOnClickListener {
                itemClickListener.onClickItem(it, position)
            }
        }
    }

    override fun getItemCount(): Int = data.size

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }
}