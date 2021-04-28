package com.example.linkit_android.mypage.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.linkit_android.chatting.adapter.ChatListAdapter
import com.example.linkit_android.chatting.adapter.ChatListData
import com.example.linkit_android.databinding.ActivityRecommendListBinding

class RecommendListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecommendListBinding

    private lateinit var recommendListAdapter: ChatListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setViewBinding()

        initRecommendListRecyclerView()
    }

    private fun setViewBinding() {
        binding = ActivityRecommendListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    private fun initRecommendListRecyclerView() {
        recommendListAdapter = ChatListAdapter(this)

        binding.recyclerviewRecommend.apply {
            adapter = recommendListAdapter
            layoutManager = LinearLayoutManager(this@RecommendListActivity)
        }

        recommendListAdapter.apply {
            data = mutableListOf(
                ChatListData("https://cdn.pixabay.com/photo/2020/03/18/19/17/easter-4945288_1280.jpg",
                    "김영만", "프론트엔드 개발", "강희원님을 추천합니다."),
                ChatListData("https://cdn.pixabay.com/photo/2021/03/02/20/21/hare-6063733_1280.jpg",
                    "고구마", "백엔드 개발", "재미있게 프로젝트 했습니다.")
            )
            notifyDataSetChanged()
        }
    }
}