package com.example.linkit_android.util

import android.view.View

interface ItemClickListener {
    fun onClickItem(view: View, position: Int)
}