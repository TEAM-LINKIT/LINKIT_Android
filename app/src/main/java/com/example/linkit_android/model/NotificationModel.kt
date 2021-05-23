package com.example.linkit_android.model

class NotificationModel {
    var to: String? = null
    var notification = Notification()
    var data = Data()

    // background push
    inner class Notification {
        lateinit var title: String
        lateinit var content: String
    }

    // foreground push
    inner class Data {
        lateinit var title: String
        lateinit var content: String
    }
}