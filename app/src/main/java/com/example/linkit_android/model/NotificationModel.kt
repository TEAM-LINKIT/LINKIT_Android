package com.example.linkit_android.model

class NotificationModel {
    var to: String? = null
    var notification = Notification()
    var data = Data()

    // background push
    inner class Notification {
        lateinit var title: String
        lateinit var body: String
        lateinit var data: String
        lateinit var android_channel_id: String
    }

    // foreground push
    inner class Data {
        lateinit var title: String
        lateinit var text: String
        lateinit var data: String
        lateinit var android_channel_id: String
    }
}