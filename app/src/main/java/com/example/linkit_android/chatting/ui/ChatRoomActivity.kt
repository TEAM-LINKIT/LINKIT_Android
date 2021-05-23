package com.example.linkit_android.chatting.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.linkit_android.chatting.adapter.ChatAdapter
import com.example.linkit_android.chatting.adapter.ChatData
import com.example.linkit_android.databinding.ActivityChatRoomBinding
import com.example.linkit_android.model.ChatModel
import com.example.linkit_android.model.UserModel
import com.example.linkit_android.util.SharedPreferenceController
import com.example.linkit_android.util.getPartString
import com.google.firebase.database.*

class ChatRoomActivity : AppCompatActivity() {

    private lateinit var chatAdapter: ChatAdapter

    private lateinit var binding: ActivityChatRoomBinding

    private lateinit var uid: String
    private lateinit var destUid: String
    private var chatRoomId: String? = null

    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference: DatabaseReference = firebaseDatabase.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setViewBinding()

        getIntentValue()

        setPref()

        setOpponentProfile()

        initChatRecyclerView()

        initSendBtn()

        initBackBtn()
    }

    private fun setViewBinding() {
        binding = ActivityChatRoomBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    private fun getIntentValue() {
        chatRoomId = null
        // Todo: ChatFragment, ProfileActivity에서 받아온 intent destId에 넣어주기
        destUid = "0JCQiGEERzMcLI7AB83gGeF4oVP2"
    }

    private fun setPref() {
        uid = SharedPreferenceController.getUid(this).toString()
    }

    private fun setOpponentProfile() {
        databaseReference.child("users").child(destUid).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val opponentProfile = snapshot.getValue(UserModel::class.java)
                binding.apply {
                    tvName.text = opponentProfile!!.userName.toString()
                    tvPart.text = getPartString(opponentProfile.userPart!!)
                    Glide.with(this@ChatRoomActivity).load(opponentProfile.profileImg).into(binding.imgProfile)
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun initChatRecyclerView() {
        chatAdapter = ChatAdapter(this)
        binding.recyclerviewChat.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(this@ChatRoomActivity)
        }
        findChatRoomId()
    }

    private fun getChatData() {
        if (chatRoomId != null)
            databaseReference.child("chat").child(chatRoomId!!).child("comments")
                    .addValueEventListener(object: ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            chatAdapter.data.clear()
                            bindMessageToRecyclerView(snapshot)
                        }
                        override fun onCancelled(error: DatabaseError) {}
                    })
    }

    private fun bindMessageToRecyclerView(data: DataSnapshot) {
        for (item in data.children) {
            val chatData = item.getValue(ChatModel::class.java)
            if (chatData!!.uid.equals(uid))
                chatAdapter.data.add(ChatData(chatData.message!!, 0))
            else
                chatAdapter.data.add(ChatData(chatData.message!!, 1))
        }
        chatAdapter.notifyDataSetChanged()
        binding.recyclerviewChat.scrollToPosition(chatAdapter.data.size - 1)
    }

    private fun initSendBtn() {
        binding.btnSend.setOnClickListener {
            pushChatData()
        }
    }

    private fun pushChatData() {
        val userList = mutableListOf(uid, destUid)
        if (binding.etChatContent.text.toString() == "")
            return
        else {
            if (chatRoomId == null)
                databaseReference.child("chat").push().child("users")
                        .setValue(userList).addOnSuccessListener {
                            findChatRoomId()
                        }
            pushComment()
        }
    }

    private fun findChatRoomId() {
        databaseReference.child("chat").addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (item in snapshot.children) {
                    val itemId = item.key.toString()
                    databaseReference.child("chat").child(item.key.toString()).child("users")
                            .addListenerForSingleValueEvent(object: ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val userList = mutableListOf<String>()
                                    for (i in snapshot.children)
                                        userList.add(i.value.toString())
                                    if ((userList[0] == uid && userList[1] == destUid)
                                            || (userList[0] == destUid && userList[1] == uid)) {
                                        chatRoomId = itemId
                                        getChatData()
                                    }
                                }
                                override fun onCancelled(error: DatabaseError) {}
                            })
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun pushComment() {
        val chatModel = ChatModel()
        chatModel.uid = uid
        chatModel.message = binding.etChatContent.text.toString()
        databaseReference.child("chat").child(chatRoomId!!)
                .child("comments").push().setValue(chatModel)
        binding.etChatContent.setText("")
    }

    private fun initBackBtn() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}