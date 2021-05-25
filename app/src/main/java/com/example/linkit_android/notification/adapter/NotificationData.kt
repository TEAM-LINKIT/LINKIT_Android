package com.example.linkit_android.notification.adapter

data class NotificationData (
    val profileImg: String,
    val name: String,
    val part: String,
    val content: CharSequence,
    val read: Boolean
)