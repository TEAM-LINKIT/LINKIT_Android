package com.example.linkit_android.chatting.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.linkit_android.chatting.adapter.ChatAdapter
import com.example.linkit_android.chatting.adapter.ChatData
import com.example.linkit_android.databinding.ActivityChatRoomBinding

class ChatRoomActivity : AppCompatActivity() {

    private lateinit var chatAdapter: ChatAdapter

    private lateinit var binding: ActivityChatRoomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setViewBinding()

        initChatRecyclerView()
    }

    private fun setViewBinding() {
        binding = ActivityChatRoomBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    private fun initChatRecyclerView() {
        chatAdapter = ChatAdapter(this)

        binding.recyclerviewChat.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(this@ChatRoomActivity)
        }

        chatAdapter.apply {
            data = mutableListOf(
                ChatData("네 작년 하반기 진행된 해커톤에 나갔습니다.", 0),
                ChatData("안녕하세요! LINK IT 서비스를 개발하고 있는 김영만 입니다. 혹시 프로젝트 진행 경험이 있으신가요?", 1),
                ChatData("네! 잘 부탁드립니다!", 0),
                ChatData("좋네요! 함께 프로젝트 진행해요~", 1),
                ChatData("네 작년 하반기 진행된 해커톤에 나갔습니다.", 0),
                ChatData("안녕하세요! LINK IT 서비스를 개발하고 있는 김영만 입니다. 혹시 프로젝트 진행 경험이 있으신가요?", 1),
                ChatData("네! 잘 부탁드립니다!", 0),
                ChatData("좋네요! 함께 프로젝트 진행해요~", 1),
                ChatData("네 작년 하반기 진행된 해커톤에 나갔습니다.", 0),
                ChatData("안녕하세요! LINK IT 서비스를 개발하고 있는 김영만 입니다. 혹시 프로젝트 진행 경험이 있으신가요?", 1),
                ChatData("네! 잘 부탁드립니다!", 0),
                ChatData("좋네요! 함께 프로젝트 진행해요~", 1),
                ChatData("네 작년 하반기 진행된 해커톤에 나갔습니다.", 0),
                ChatData("안녕하세요! LINK IT 서비스를 개발하고 있는 김영만 입니다. 혹시 프로젝트 진행 경험이 있으신가요?", 1),
                ChatData("네! 잘 부탁드립니다!", 0)
            )
            notifyDataSetChanged()
        }
    }
}