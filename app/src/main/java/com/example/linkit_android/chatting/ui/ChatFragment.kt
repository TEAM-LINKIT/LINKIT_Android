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
import com.example.linkit_android.model.ChatModel
import com.example.linkit_android.util.SharedPreferenceController
import com.example.linkit_android.util.getPartString
import com.google.firebase.database.*

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private lateinit var uid: String

    private lateinit var chatListAdapter: ChatListAdapter

    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference: DatabaseReference = firebaseDatabase.reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setPref()

        initChatListRecyclerView()

        checkMyChatRoom()
    }

    private fun setPref() {
        uid = SharedPreferenceController.getUid(context!!).toString()
    }

    private fun initChatListRecyclerView() {
        chatListAdapter = ChatListAdapter(context!!)
        binding.recyclerviewChat.apply {
            adapter = chatListAdapter
            layoutManager = LinearLayoutManager(context!!)
        }
    }

    private fun checkMyChatRoom() {
        databaseReference.child("chat").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chatListAdapter.data.clear()
                for (item in snapshot.children) {
                    val itemId = item.key.toString()
                    databaseReference.child("chat").child(itemId).child("users")
                            .addListenerForSingleValueEvent(object: ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    var chatRoomId = ""
                                    var destUserId = ""
                                    if (snapshot.child("0").value.toString() == uid) {
                                        chatRoomId = itemId
                                        destUserId = snapshot.child("1").value.toString()
                                        getLastComment(chatRoomId, destUserId)
                                    } else if (snapshot.child("1").value.toString() == uid) {
                                        chatRoomId = itemId
                                        destUserId = snapshot.child("0").value.toString()
                                        getLastComment(chatRoomId, destUserId)
                                    }
                                }
                                override fun onCancelled(error: DatabaseError) {}
                            })
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun getLastComment(chatRoomId: String, destUserId: String) {
        var lastComment = ""
        databaseReference.child("chat").child(chatRoomId).child("comments")
                .addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for ((count,item) in snapshot.children.withIndex()) {
                            if (count == snapshot.childrenCount.toString().toInt() - 1) {
                                val chatItem = item.getValue(ChatModel::class.java)
                                lastComment = chatItem!!.message.toString()
                                bindDateToRecyclerView(destUserId, lastComment)
                            }
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {}
                })
    }

    private fun bindDateToRecyclerView(destUserId: String, lastComment: String) {
        databaseReference.child("users").child(destUserId)
                .addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val profileImg = snapshot.child("profileImg").value.toString()
                        val name = snapshot.child("userName").value.toString()
                        val part = getPartString(snapshot.child("userPart").value.toString().toInt())
                        chatListAdapter.data.add(ChatListData(profileImg, name, part, lastComment))
                        chatListAdapter.notifyDataSetChanged()
                    }
                    override fun onCancelled(error: DatabaseError) {}
                })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}