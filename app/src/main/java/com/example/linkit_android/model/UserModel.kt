package com.example.linkit_android.model

data class UserModel (
    val uid: String,
    val id: String,
    val userName: String,
    val userPart: Int,
    val profileImg: String,
    val pushToken: String
        )