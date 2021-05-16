package com.example.linkit_android.community.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.linkit_android.R
import com.example.linkit_android.util.ItemClickListener
import kotlinx.android.synthetic.main.item_community.view.*

class CommunityAdapter(private val context: Context) : RecyclerView.Adapter<CommunityViewHolder>() {

    var data = mutableListOf<CommunityData>()
    private lateinit var itemClickListener: ItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_community, parent, false)
        return CommunityViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommunityViewHolder, position: Int) {
        holder.apply {
            onBind(data[position])
            itemView.cardview_community.setOnClickListener {
                itemClickListener.onClickItem(it, position)
            }
        }
    }

    override fun getItemCount(): Int = data.size

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }
}