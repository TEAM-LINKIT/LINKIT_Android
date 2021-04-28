package com.example.linkit_android.chatting.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.linkit_android.chatting.adapter.ChatListAdapter
import com.example.linkit_android.chatting.adapter.ChatListData
import com.example.linkit_android.databinding.FragmentChatBinding

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private lateinit var chatListAdapter: ChatListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initChatListRecyclerView()
    }

    private fun initChatListRecyclerView() {
        chatListAdapter = ChatListAdapter(context!!)

        binding.recyclerviewChat.apply {
            adapter = chatListAdapter
            layoutManager = LinearLayoutManager(context!!)
        }

        chatListAdapter.apply {
            data = mutableListOf(
                    ChatListData("https://cdn.pixabay.com/photo/2020/03/18/19/17/easter-4945288_1280.jpg",
                            "김영만", "프론트엔드 개발", "네 작년 하반기 진행된 해커톤에 나갔습니다."),
                    ChatListData("https://cdn.pixabay.com/photo/2021/03/02/20/21/hare-6063733_1280.jpg",
                            "고구마", "백엔드 개발", "서울에 거주하고 있습니다."),
                    ChatListData("https://cdn.pixabay.com/photo/2021/03/02/20/21/hare-6063733_1280.jpg",
                            "김모피", "기획", "안녕하세요! 프론트엔드 개발 파트 지원하고 싶은 강희원입니다. 다름이 아니라 ")
            )
            notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}