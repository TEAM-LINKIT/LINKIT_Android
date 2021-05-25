package com.example.linkit_android.upload.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.linkit_android.R
import com.example.linkit_android.databinding.DialogBasicBinding
import com.example.linkit_android.model.NotificationListModel
import com.example.linkit_android.model.NotificationModel
import com.example.linkit_android.util.SharedPreferenceController
import com.example.linkit_android.util.setDialogSize
import com.google.firebase.database.*
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import java.io.IOException
import java.sql.Timestamp

class SendPortfolioDialogFragment : DialogFragment() {

    private var _binding: DialogBasicBinding? = null
    private val binding get() = _binding!!

    private lateinit var uid: String
    private lateinit var userName: String
    private lateinit var destUid: String
    private lateinit var destPushToken: String
    private lateinit var postingId: String
    private lateinit var postingTitle: String

    private val firebaseDatabase : FirebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference : DatabaseReference = firebaseDatabase.reference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _binding = DialogBasicBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setPref()

        getBundle()

        setContentTextView()

        initOkBtn()

        initCancelBtn()
    }

    private fun setPref() {
        uid = SharedPreferenceController.getUid(requireContext()).toString()
        userName = SharedPreferenceController.getUserName(requireContext()).toString()
    }

    private fun getBundle() {
        val destUserList = requireArguments().getStringArrayList("destUserInfo")!!
        destUid = destUserList[0]
        destPushToken = destUserList[1]
        postingId = destUserList[2]
        postingTitle = destUserList[3]
    }

    private fun setContentTextView() {
        binding.tvContent.text = "포트폴리오를 전송하시겠습니까?"
    }

    private fun initOkBtn() {
        binding.buttonOk.setOnClickListener {
            pushPortfolioToDestUid()
        }
    }

    private fun pushPortfolioToDestUid() {
        val timeStamp = Timestamp(System.currentTimeMillis()).time.toString()
        val notificationListModel = NotificationListModel(uid, postingId, false)
        databaseReference.child("users").child(destUid).child("notification")
                .child(timeStamp).setValue(notificationListModel).addOnSuccessListener { sendFcm() }
    }

    private fun sendFcm() {
        val gson = Gson()
        val notificationModel = NotificationModel()
        val message = "${userName}님이 포트폴리오를 전송했습니다."

        notificationModel.apply {
            to = destPushToken
            notification.title = postingTitle
            notification.body = message
            notification.data = uid
            notification.android_channel_id = getString(R.string.firebase_portfolio_notification_channel_id)
        }
        notificationModel.data.apply {
            title = postingTitle
            text = message
            data = uid
            android_channel_id = getString(R.string.firebase_portfolio_notification_channel_id)
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
                this@SendPortfolioDialogFragment.dismiss()
            }
        })
    }

    private fun initCancelBtn() {
        binding.buttonCancel.setOnClickListener {
            this.dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        setDialogSize(dialog, activity)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}