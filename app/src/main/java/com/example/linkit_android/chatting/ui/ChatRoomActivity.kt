package com.example.linkit_android.chatting.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.linkit_android.chatting.adapter.ChatAdapter
import com.example.linkit_android.chatting.adapter.ChatData
import com.example.linkit_android.databinding.ActivityChatRoomBinding
import com.example.linkit_android.model.ChatModel
import com.example.linkit_android.model.NotificationModel
import com.example.linkit_android.model.UserModel
import com.example.linkit_android.util.SharedPreferenceController
import com.example.linkit_android.util.getPartString
import com.google.firebase.database.*
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import java.io.IOException

class ChatRoomActivity : AppCompatActivity() {

    private lateinit var chatAdapter: ChatAdapter

    private lateinit var binding: ActivityChatRoomBinding

    private lateinit var uid: String
    private lateinit var destUid: String
    private lateinit var destUserName: String
    private lateinit var destPushToken: String
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
        destUid = intent.getStringExtra("chatRoomId").toString()
    }

    private fun setPref() {
        uid = SharedPreferenceController.getUid(this).toString()
    }

    private fun setOpponentProfile() {
        databaseReference.child("users").child(destUid).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val opponentProfile = snapshot.getValue(UserModel::class.java)
                destPushToken = opponentProfile!!.pushToken.toString()
                binding.apply {
                    destUserName = opponentProfile.userName.toString()
                    tvName.text = destUserName
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
        findChatRoomId(false)
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
                chatAdapter.data.add(ChatData(chatData.message!!, 1))
            else
                chatAdapter.data.add(ChatData(chatData.message!!, 0))
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
                            findChatRoomId(true)
                        }
            else
                pushComment()
        }
    }

    private fun findChatRoomId(commentExist: Boolean) {
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
                                        if (commentExist)
                                            pushComment()
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
        val chatContent = binding.etChatContent.text.toString()
        val chatModel = ChatModel()
        chatModel.uid = uid
        chatModel.message = chatContent
        databaseReference.child("chat").child(chatRoomId!!)
                .child("comments").push().setValue(chatModel).addOnSuccessListener {
                sendFcm(chatContent)
            }
    }

    private fun sendFcm(pushMessage: String) {
        val gson = Gson()
        val notificationModel = NotificationModel()

        notificationModel.apply {
            to = destPushToken
            notification.title = destUserName
            notification.text = pushMessage
        }
        notificationModel.data.apply {
            title = destUserName
            text = pushMessage
        }

        val requestBody = RequestBody.create("application/json; charset=utf8".toMediaType(),
            gson.toJson(notificationModel))

        val request = Request.Builder()
            .header("Content-Type", "application/json")
            .addHeader("Authorization", "key=AAAAE_OflkE:APA91bHApt6oyK9MzvIiADDqTYznEs_PMZndn85Oh5t1ju3_ZUznl90RDEeR9KPsgGiApJYAOuw1sjCrgr1VtXVo315hczN7ZGeA_ldkcBXPuwgFqM9mloEXc70lbqJ4P-rV79o_FInv")
            .url("https://fcm.googleapis.com/fcm/send")
            .post(requestBody)
            .build()

        val okHttpClient = OkHttpClient()
        okHttpClient.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                binding.etChatContent.setText("")
            }
        })
    }

    private fun initBackBtn() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}