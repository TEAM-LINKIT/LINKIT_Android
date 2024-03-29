package com.example.linkit_android.profile.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.linkit_android.chatting.ui.ChatRoomActivity
import com.example.linkit_android.databinding.ActivityProfileBinding
import com.example.linkit_android.portfolio.adapter.ProjectAdapter
import com.example.linkit_android.portfolio.adapter.ProjectData
import com.example.linkit_android.portfolio.adapter.TagAdapter
import com.example.linkit_android.util.SharedPreferenceController
import com.example.linkit_android.util.getPartString
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.firebase.database.*

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    private lateinit var projectAdapter: ProjectAdapter
    private lateinit var toolAdapter: TagAdapter
    private lateinit var fieldAdapter: TagAdapter
    private lateinit var recommendAdapter: ProjectAdapter

    private lateinit var toolList: ArrayList<String>
    private lateinit var fieldList: ArrayList<String>

    private val firebaseDatabase : FirebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference : DatabaseReference = firebaseDatabase.reference

    private lateinit var writerId : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        writerId = intent.getStringExtra("writerId").toString()

        setViewBinding()

        initProjectRecyclerView()

        initToolRecyclerView()

        initFieldRecyclerView()

        initRecommendRecyclerView()

        initBackBtn()

        initChatBtn()

        initRecommendBtn()

        initProfile()

        getTagDataFromFirebase()

    }

    private fun setViewBinding() {
        binding = ActivityProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    private fun initBackBtn() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun initChatBtn() {
        binding.btnChat.setOnClickListener {
            val intent = Intent(this, ChatRoomActivity::class.java)
            intent.putExtra("chatRoomId", writerId)
            startActivity(intent)
        }
    }

    private fun initRecommendBtn() {
        val uid = SharedPreferenceController.getUid(this).toString()
        binding.btnRecommend.setOnClickListener {
            if (writerId == uid) {
                Toast.makeText(this, "자기자신을 추천할 수 없습니다", Toast.LENGTH_SHORT).show()
            }
            else {
                val intent = Intent(this, RecommendActivity::class.java)
                intent.putExtra("writerId", writerId)
                startActivityForResult(intent, 1)
            }
        }
    }

    private fun initRecommendComment(writername : String) {
        val username = SharedPreferenceController.getUserName(this).toString()

        binding.tvRecommend.text = username + "님,\n" + writername + "님을 추천해주세요."
    }

    private fun initProfile() {
        databaseReference.child("users").child(writerId).addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val writerName = snapshot.child("userName").value.toString()

                        binding.tvName.text = writerName

                        /* 자기소개 불러오기 */

                        binding.tvContentIntroduce.text = snapshot.child("portfolio").child("introduction").value.toString()

                        if(binding.tvContentIntroduce.text == "null"){
                            binding.tvContentIntroduce.visibility = View.GONE
                            binding.tvNoneIntroduce.visibility = View.VISIBLE
                        }
                        else {
                            binding.tvContentIntroduce.visibility = View.VISIBLE
                            binding.tvNoneIntroduce.visibility = View.GONE
                        }

                        /* 학력 불러오기 */

                        binding.tvSchool.text = snapshot.child("portfolio").child("education").child("0").value.toString()

                        if(binding.tvSchool.text == "null"){
                            binding.tvNoneEducation.visibility = View.VISIBLE
                            binding.tvMajor.visibility = View.GONE
                            binding.tvSchool.visibility = View.GONE
                        }
                        else {
                            binding.tvNoneEducation.visibility = View.GONE
                            binding.tvMajor.visibility = View.VISIBLE
                            binding.tvSchool.visibility = View.VISIBLE
                        }

                        binding.tvPart.text = getPartString(snapshot.child("userPart").value.toString().toInt())

                        Glide.with(this@ProfileActivity).load(snapshot.child("profileImg").value.toString()).into(binding.imgProfile)
                        initRecommendComment(writerName)
                    }
                    override fun onCancelled(error: DatabaseError) {}
                })
    }


    private fun initProjectRecyclerView() {
        val projectList = mutableListOf<ProjectData>()

        projectAdapter = ProjectAdapter(this)

        binding.recyclerviewProject.apply {
            adapter = projectAdapter
            layoutManager = LinearLayoutManager(this@ProfileActivity)
        }

        databaseReference.child("users").child(writerId).child("portfolio").child("project")
                .addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (!snapshot.exists()) {
                            binding.tvNoneProject.visibility = View.VISIBLE
                            binding.recyclerviewProject.visibility = View.GONE
                        }
                        else {
                            for(item in snapshot.children) {
                                projectList.add(ProjectData(item.child("projectImg").value.toString(), item.child("title").value.toString(),
                                        item.child("content").value.toString(), item.child("link").value.toString()))
                            }
                            projectAdapter.data = projectList
                            projectAdapter.notifyDataSetChanged()
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {}
                })
    }

    private fun initToolRecyclerView() {
        toolList = arrayListOf()
        toolAdapter = TagAdapter(this)

        FlexboxLayoutManager(this).apply {
            flexWrap = FlexWrap.WRAP
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
        }.let {
            binding.recyclerviewTool.layoutManager = it
            binding.recyclerviewTool.adapter = toolAdapter
        }
    }

    private fun initFieldRecyclerView() {
        fieldList = arrayListOf()
        fieldAdapter = TagAdapter(this)

        FlexboxLayoutManager(this).apply {
            flexWrap = FlexWrap.WRAP
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
        }.let {
            binding.recyclerviewField.layoutManager = it
            binding.recyclerviewField.adapter = fieldAdapter
        }
    }

    private fun getTagDataFromFirebase() {
        databaseReference.child("users").child(writerId).child("portfolio")
                .addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        toolList.clear()
                        if (snapshot.child("tool").exists())
                            for (item in snapshot.child("tool").children)
                                toolList.add(item.value.toString())
                        else
                            toolList = arrayListOf()
                        bindToolData()

                        fieldList.clear()
                        if (snapshot.child("field").exists())
                            for (item in snapshot.child("field").children)
                                fieldList.add(item.value.toString())
                        else
                            fieldList = arrayListOf()
                        bindFieldData()
                    }
                    override fun onCancelled(error: DatabaseError) {}
                })
    }

    private fun bindToolData() {
        if (toolList.isEmpty()) {
            binding.recyclerviewTool.visibility = View.GONE
            binding.tvNoneTool.visibility = View.VISIBLE
        } else {
            binding.recyclerviewTool.visibility = View.VISIBLE
            binding.tvNoneTool.visibility = View.GONE
            toolAdapter.apply {
                data = toolList
                notifyDataSetChanged()
            }
        }
    }

    private fun bindFieldData() {
        if (fieldList.isEmpty()) {
            binding.recyclerviewField.visibility = View.GONE
            binding.tvNoneField.visibility = View.VISIBLE
        } else {
            binding.recyclerviewField.visibility = View.VISIBLE
            binding.tvNoneField.visibility = View.GONE
            fieldAdapter.apply {
                data = fieldList
                notifyDataSetChanged()
            }
        }
    }

    private fun initRecommendRecyclerView() {
        val recommendList = mutableListOf<ProjectData>()

        recommendAdapter = ProjectAdapter(this)

        binding.recyclerviewRecommend.apply {
            adapter = recommendAdapter
            layoutManager = LinearLayoutManager(this@ProfileActivity)
        }

        databaseReference.child("users").child(writerId).child("recommend")
                .addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (!snapshot.exists()) {
                            binding.tvNoneRecommend.visibility = View.VISIBLE
                            binding.recyclerviewRecommend.visibility = View.GONE
                        }
                        else {
                            for(item in snapshot.children) {
                                recommendList.add(ProjectData(item.child("projectImg").value.toString(), item.child("title").value.toString(),
                                        item.child("content").value.toString(), item.child("link").value.toString()))
                            }
                            recommendAdapter.data = recommendList
                            recommendAdapter.notifyDataSetChanged()
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {}
                })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                1 -> {
                    binding.tvNoneRecommend.visibility = View.GONE
                    binding.recyclerviewRecommend.visibility = View.VISIBLE
                    initRecommendRecyclerView()
                }
            }
        }
    }

}