package com.example.linkit_android.profile.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.linkit_android.databinding.ActivityProfileBinding
import com.example.linkit_android.portfolio.adapter.ProjectAdapter
import com.example.linkit_android.portfolio.adapter.ProjectData
import com.example.linkit_android.portfolio.adapter.TagAdapter
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    private lateinit var projectAdapter: ProjectAdapter
    private lateinit var toolAdapter: TagAdapter
    private lateinit var fieldAdapter: TagAdapter
    private lateinit var recommendAdapter: ProjectAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setViewBinding()

        initProjectRecyclerView()

        initToolRecyclerView()

        initFieldRecyclerView()

        initRecommendRecyclerView()
    }

    private fun setViewBinding() {
        binding = ActivityProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    private fun initProjectRecyclerView() {
        projectAdapter = ProjectAdapter(this)

        binding.recyclerviewProject.apply {
            adapter = projectAdapter
            layoutManager = LinearLayoutManager(this@ProfileActivity)
        }

        projectAdapter.data = mutableListOf(
            ProjectData("https://cdn.pixabay.com/photo/2020/03/18/19/17/easter-4945288_1280.jpg", "DayBreak", "당신의 하루를 깨우는 습관 형성 앱", ""),
            ProjectData("https://cdn.pixabay.com/photo/2021/03/02/20/21/hare-6063733_1280.jpg", "LINK IT", "IT 프로젝트 팀 빌딩 & 네트워킹 플랫폼", "")
        )
        projectAdapter.notifyDataSetChanged()
    }

    private fun initToolRecyclerView() {
        toolAdapter = TagAdapter(this)

        FlexboxLayoutManager(this).apply {
            flexWrap = FlexWrap.WRAP
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
        }.let {
            binding.recyclerviewTool.layoutManager = it
            binding.recyclerviewTool.adapter = toolAdapter
        }

        toolAdapter.apply {
            data = mutableListOf("여기는 협업툴 자리", "Github", "Slack", "Notion", "Trello", "Figma")
            notifyDataSetChanged()
        }
    }

    private fun initFieldRecyclerView() {
        fieldAdapter = TagAdapter(this)

        FlexboxLayoutManager(this).apply {
            flexWrap = FlexWrap.WRAP
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
        }.let {
            binding.recyclerviewField.layoutManager = it
            binding.recyclerviewField.adapter = fieldAdapter
        }

        fieldAdapter.apply {
            data = mutableListOf("여기는 활동 분야 자리", "Android", "Kotlin", "GitHub", "Java")
            notifyDataSetChanged()
        }
    }

    private fun initRecommendRecyclerView() {
        recommendAdapter = ProjectAdapter(this)

        binding.recyclerviewRecommend.apply {
            adapter = recommendAdapter
            layoutManager = LinearLayoutManager(this@ProfileActivity)
        }

        recommendAdapter.data = mutableListOf(
            ProjectData("https://cdn.pixabay.com/photo/2020/03/18/19/17/easter-4945288_1280.jpg", "김안드", "강희원님을 추천합니다.", ""),
            ProjectData("https://cdn.pixabay.com/photo/2021/03/02/20/21/hare-6063733_1280.jpg", "고구마", "재미있게 프로젝트 했습니다!", "")
        )
        recommendAdapter.notifyDataSetChanged()
    }
}