package com.example.linkit_android.chatting.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.linkit_android.R
import com.example.linkit_android.util.ItemClickListener
import kotlinx.android.synthetic.main.item_chat.view.*

class ChatListAdapter(private val context: Context) : RecyclerView.Adapter<ChatListViewHolder>() {

    var data = mutableListOf<ChatListData>()
    private lateinit var itemClickListener: ItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false)
        return ChatListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatListViewHolder, position: Int) {
        holder.apply {
            onBind(data[position])
            itemView.constraintlayout_chat_list.setOnClickListener {
                itemClickListener.onClickItem(it, position)
            }
        }
    }

    override fun getItemCount(): Int = data.size

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }
}