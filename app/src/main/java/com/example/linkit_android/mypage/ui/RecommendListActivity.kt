package com.example.linkit_android.mypage.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.linkit_android.chatting.adapter.ChatListAdapter
import com.example.linkit_android.chatting.adapter.ChatListData
import com.example.linkit_android.databinding.ActivityRecommendListBinding
import com.example.linkit_android.portfolio.adapter.ProjectData
import com.example.linkit_android.util.SharedPreferenceController
import com.example.linkit_android.util.getPartString
import com.google.firebase.database.*

class RecommendListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecommendListBinding

    private lateinit var recommendListAdapter: ChatListAdapter

    private val firebaseDatabase : FirebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference : DatabaseReference = firebaseDatabase.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setViewBinding()

        initBackBtn()

        initRecommendListRecyclerView()
    }

    private fun setViewBinding() {
        binding = ActivityRecommendListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    private fun initBackBtn() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun initRecommendListRecyclerView() {
        val recommendList = mutableListOf<ChatListData>()
        val uid = SharedPreferenceController.getUid(this).toString()
        var writerpart = ""

        recommendListAdapter = ChatListAdapter(this)

        binding.recyclerviewRecommend.apply {
            adapter = recommendListAdapter
            layoutManager = LinearLayoutManager(this@RecommendListActivity)
        }

        databaseReference.child("users").child(uid).child("recommend")
                .addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (!snapshot.exists()) {
                            binding.recyclerviewRecommend.visibility = View.GONE
                        }
                        else {
                            for(item in snapshot.children) {
                                writerpart = getPartString(item.child("writerpart").value.toString().toInt())
                                recommendList.add(ChatListData(item.child("projectImg").value.toString(), item.child("title").value.toString(),
                                        writerpart, item.child("content").value.toString()))
                            }
                            recommendListAdapter.data = recommendList
                            recommendListAdapter.notifyDataSetChanged()
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {}
                })
        }
    }
