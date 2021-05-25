package com.example.linkit_android.notification.ui

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.StyleSpan
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.linkit_android.databinding.ActivityNotificationBinding
import com.example.linkit_android.model.NotificationListModel
import com.example.linkit_android.model.UserModel
import com.example.linkit_android.notification.adapter.NotificationAdapter
import com.example.linkit_android.notification.adapter.NotificationData
import com.example.linkit_android.profile.ui.ProfileActivity
import com.example.linkit_android.util.ItemClickListener
import com.example.linkit_android.util.SharedPreferenceController
import com.example.linkit_android.util.getPartString
import com.google.firebase.database.*
import java.util.*

class NotificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationBinding

    private lateinit var notificationAdapter: NotificationAdapter
    private lateinit var notificationList: MutableList<NotificationListModel>
    private lateinit var notificationKeyList: MutableList<String>
    private lateinit var userList: MutableList<UserModel>

    private lateinit var uid: String

    private val firebaseDatabase : FirebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference : DatabaseReference = firebaseDatabase.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setViewBinding()

        setPref()

        initBackBtn()
    }

    private fun setViewBinding() {
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    private fun setPref() {
        uid = SharedPreferenceController.getUid(this).toString()
    }

    private fun initNotificationRecyclerView() {
        notificationAdapter = NotificationAdapter(this)
        binding.recyclerviewPortfolio.apply {
            adapter = notificationAdapter
            layoutManager = LinearLayoutManager(this@NotificationActivity)
        }
        getNotificationData()
    }

    private fun getNotificationData() {
        notificationList = mutableListOf()
        notificationList.clear()
        notificationKeyList = mutableListOf()
        notificationKeyList.clear()
        databaseReference.child("users").child(uid).child("notification")
                .addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (item in snapshot.children) {
                                val notificationData = item.getValue(NotificationListModel::class.java)
                                notificationList.add(notificationData!!)
                                notificationKeyList.add(item.key.toString())
                            }
                            getNotificationUserProfile()
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {}
                })
    }

    private fun getNotificationUserProfile() {
        userList = mutableListOf()
        userList.clear()
        for ((count, item) in notificationList.withIndex()) {
            databaseReference.child("users").child(item.uid!!)
                    .addListenerForSingleValueEvent(object: ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            userList.add(snapshot.getValue(UserModel::class.java)!!)
                            if (count == notificationList.size - 1) {
                                userList.reverse()
                                notificationKeyList.reverse()
                                notificationList.reverse()
                                bindDataToRecyclerView()
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {}
                    })
        }
    }

    private fun bindDataToRecyclerView() {
        for ((i, item) in userList.withIndex())
            notificationAdapter.data.add(
                    NotificationData(
                            item.profileImg!!,
                            item.userName!!,
                            getPartString(item.userPart!!),
                            getNotificationContent(item.userName!!, notificationList[i].postingTitle!!),
                            notificationList[i].read!!))
        notificationAdapter.notifyDataSetChanged()
        initItemClickListener()
    }

    private fun getNotificationContent(userName: String, title: String) : CharSequence {
        var notificationContent: CharSequence = ""
        val spannableName = SpannableStringBuilder(userName)
        spannableName.setSpan(StyleSpan(Typeface.BOLD), 0, userName.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        notificationContent = TextUtils.concat(notificationContent, spannableName)
        notificationContent = TextUtils.concat(notificationContent, "님이 ")

        var splitTitle = ""
        if (title.length > 30)  {
            splitTitle = title.substring(0, 29)
            splitTitle = "${splitTitle}…"
        } else splitTitle = title

        val spannableTitle = SpannableStringBuilder(splitTitle)
        spannableTitle.setSpan(StyleSpan(Typeface.BOLD), 0, splitTitle.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        notificationContent = TextUtils.concat(notificationContent, spannableTitle)
        notificationContent = TextUtils.concat(notificationContent, " 글에 포트폴리오를 전송했습니다.")

        return notificationContent
    }

    private fun initItemClickListener() {
        notificationAdapter.setItemClickListener(object: ItemClickListener {
            override fun onClickItem(view: View, position: Int) {
                val destUid = userList[position].uid
                val intent = Intent(this@NotificationActivity, ProfileActivity::class.java)
                intent.putExtra("writerId", destUid)
                databaseReference.child("users").child(uid).child("notification")
                        .child(notificationKeyList[position]).child("read").setValue(true)
                        .addOnSuccessListener { startActivity(intent) }
            }
        })
    }

    private fun initBackBtn() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        initNotificationRecyclerView()
    }
}