package com.example.linkit_android.chatting.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.linkit_android.databinding.ItemChatLeftContentBinding
import com.example.linkit_android.databinding.ItemChatRightContentBinding

class ChatAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data = mutableListOf<ChatData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            CHAT_LEFT -> {
                val binding =
                    ItemChatLeftContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                LeftChatViewHolder(binding)
            }
            else -> {
                val binding =
                    ItemChatRightContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                RightChatViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is LeftChatViewHolder) {
            holder.onBind(data[position])
        } else if (holder is RightChatViewHolder) {
            holder.onBind(data[position])
        }
    }

    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int): Int {
        return if (data.isNotEmpty())
            data[position].viewType
        else
            return super.getItemViewType(position)
    }

    // 뷰 타입 종류
    companion object {
        const val CHAT_LEFT = 0
    }
}