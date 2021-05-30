package com.example.linkit_android.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.linkit_android.R
import com.example.linkit_android.chatting.ui.ChatRoomActivity
import com.example.linkit_android.notification.ui.NotificationActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = "FirebaseService"

    override fun onNewToken(token: String) {
        Log.d(TAG, "new Token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "from: " + remoteMessage.from)

        // background push, foreground push 구분
        when {
            remoteMessage.data.isNotEmpty() -> {
                sendNotification(remoteMessage.data["text"]!!, remoteMessage.data["title"]!!,
                        remoteMessage.data["android_channel_id"]!!, remoteMessage)
            }
            remoteMessage.notification != null -> {
                sendNotification(remoteMessage.notification!!.body!!, remoteMessage.notification!!.title!!,
                        remoteMessage.notification!!.channelId!!, remoteMessage)
            }
            else -> {
                Log.d("error", "메시지를 수신하지 못했습니다.")
                Log.d("data", remoteMessage.data.toString())
            }
        }
    }

    private fun sendNotification(body: String, title: String, channelId: String, remoteMessage: RemoteMessage) {
        val uniId: Int = (System.currentTimeMillis() / 7).toInt()

        // 알림 채널에 따라 구분
        val pendingIntent = if (channelId == getString(R.string.firebase_notification_channel_id)) {
            val intent = Intent(this, ChatRoomActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.putExtra("chatRoomId", remoteMessage.data["data"])
            PendingIntent.getActivity(this, uniId, intent, PendingIntent.FLAG_ONE_SHOT)
        } else {
            val intent = Intent(this, NotificationActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            PendingIntent.getActivity(this, uniId, intent, PendingIntent.FLAG_ONE_SHOT)
        }

        // 알림 소리
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher_main)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(soundUri)
            .setPriority(Notification.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Notice", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        // 알림 생성
        notificationManager.notify(uniId, notificationBuilder.build())
    }
}