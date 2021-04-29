package com.example.linkit_android.mypage.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.linkit_android.databinding.ActivityPostingListBinding
import com.example.linkit_android.mypage.adapter.PostingListAdapter
import com.example.linkit_android.mypage.adapter.PostingListData

class PostingListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostingListBinding

    private lateinit var postingListAdapter: PostingListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setViewBinding()

        initPostingListRecyclerView()
    }

    private fun setViewBinding() {
        binding = ActivityPostingListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    private fun initPostingListRecyclerView() {
        postingListAdapter = PostingListAdapter(this)

        binding.recyclerviewPostingList.apply {
            adapter = postingListAdapter
            layoutManager = LinearLayoutManager(this@PostingListActivity)
        }

        postingListAdapter.apply {
            data = mutableListOf(
                    PostingListData("함께 웹 개발 하실 분 구해요!", "강희원 · 2021.03.21"),
                    PostingListData("함께 서울시 관광 도우미 앱 개발하실 안드로이드 유경험자 팀원을 모집합니다!", "강희원 · 2021.03.22"),
                    PostingListData("함께 서울시 관광 도우미 앱 개발하실 안드로이드 유경험자 팀원을 모집합니다!", "강희원 · 2021.03.23")
            )
            notifyDataSetChanged()
        }
    }
}