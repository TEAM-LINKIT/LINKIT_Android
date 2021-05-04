package com.example.linkit_android.notification.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.linkit_android.databinding.ActivityNotificationBinding
import com.example.linkit_android.notification.adapter.NotificationAdapter
import com.example.linkit_android.notification.adapter.NotificationData

class NotificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationBinding

    private lateinit var notificationAdapter: NotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setViewBinding()

        initNotificationRecyclerView()
    }

    private fun setViewBinding() {
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    private fun initNotificationRecyclerView() {
        notificationAdapter = NotificationAdapter(this)

        binding.recyclerviewPortfolio.apply {
            adapter = notificationAdapter
            layoutManager = LinearLayoutManager(this@NotificationActivity)
        }

        notificationAdapter.apply {
            data = mutableListOf(
                NotificationData("https://cdn.pixabay.com/photo/2016/01/19/14/55/bunny-1149060__340.jpg",
                    "김영민", "프론트엔드 개발", "최고의 리더 강선생님과 함께하는 공모전 나가실분 있나요?"),
                NotificationData("https://cdn.pixabay.com/photo/2019/07/30/05/53/dog-4372036__340.jpg",
                    "강희원", "프론트엔드 개발", "모피 텔레콤 공모전 도전하실 분 구합니다."),
                NotificationData("https://cdn.pixabay.com/photo/2017/01/14/12/59/iceland-1979445__340.jpg",
                    "김명신", "프론트엔드 개발", "함께 앱개발 하실 분 있으신가요?")
            )
            notifyDataSetChanged()
        }
    }
}