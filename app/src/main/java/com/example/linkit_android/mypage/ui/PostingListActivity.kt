package com.example.linkit_android.mypage.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.linkit_android.databinding.ActivityPostingListBinding
import com.example.linkit_android.mypage.adapter.PostingListAdapter
import com.example.linkit_android.mypage.adapter.PostingListData
import com.example.linkit_android.portfolio.adapter.ProjectData
import com.example.linkit_android.portfolio.ui.PortfolioFragment
import com.example.linkit_android.util.SharedPreferenceController
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class PostingListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostingListBinding

    private lateinit var postingListAdapter: PostingListAdapter

    private val firebaseStorage = FirebaseStorage.getInstance()
    private val firebaseDatabase : FirebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference : DatabaseReference = firebaseDatabase.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setViewBinding()

        initPostingListRecyclerView()

        initBackBtn()
    }

    private fun setViewBinding() {
        binding = ActivityPostingListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    private fun initBackBtn() {
        binding.btnBack.setOnClickListener {
            var intent = Intent(this, MypageFragment::class.java)
            finish()
        }
    }

    private fun initPostingListRecyclerView() {
        val uid = SharedPreferenceController.getUid(this).toString()
        val uname = SharedPreferenceController.getUserName(this).toString()
        var date = ""
        var nullCheck = false

        val postingList = mutableListOf<PostingListData>()

        postingListAdapter = PostingListAdapter(this)

        binding.recyclerviewPostingList.apply {
            adapter = postingListAdapter
            layoutManager = LinearLayoutManager(this@PostingListActivity)
        }

        databaseReference.child("community").addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(item in snapshot.children) {
                    if(uid == item.child("writer").value.toString()) {
                        date = getDate(item.key!!.toLong())
                        postingList.add(PostingListData(item.child("title").value.toString(), "$uname · $date"))
                        nullCheck = true
                    }
                }
                if(!nullCheck) {
                    binding.recyclerviewPostingList.visibility = View.GONE
                }
                postingListAdapter.data = postingList
                postingListAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun getDate(timestamp: Long) :String {
        val date = DateFormat.format("yyyy.MM.dd",timestamp).toString()
        return date
    }
}
