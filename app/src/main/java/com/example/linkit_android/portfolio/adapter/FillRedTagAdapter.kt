package com.example.linkit_android.portfolio.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.linkit_android.R
import com.example.linkit_android.util.ItemClickListener
import kotlinx.android.synthetic.main.item_tag_fill_red.view.*

class FillRedTagAdapter(private val context: Context) : RecyclerView.Adapter<FillRedTagViewHolder>() {

    var data = mutableListOf<String>()
    private lateinit var itemClickListener: ItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FillRedTagViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_tag_fill_red, parent, false)
        return FillRedTagViewHolder(view)
    }

    override fun onBindViewHolder(holder: FillRedTagViewHolder, position: Int) {
        holder.apply {
            onBind(data[position])
            itemView.item_close_fill.setOnClickListener {
                removeItem(position)
                itemClickListener.onClickItem(it, position)
            }
        }
    }

    override fun getItemCount(): Int = data.size

    private fun removeItem(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }
}