package com.example.linkit_android.util

import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.FragmentActivity

/* 확장함수 */

/* dialog size 설정 함수 */
fun setDialogSize(dialog: Dialog?, activity: FragmentActivity?) {
    val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
    val windowManager = activity?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = windowManager.defaultDisplay
    val size = Point()
    display.getSize(size)
    val deviceWidth = size.x
    params?.width = (deviceWidth * 0.7).toInt()
    dialog?.window?.attributes = params as WindowManager.LayoutParams
}