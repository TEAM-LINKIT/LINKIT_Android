package com.example.linkit_android.upload.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.linkit_android.databinding.ActivityPostingBinding

class PostingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setViewBinding()
    }

    private fun setViewBinding() {
        binding = ActivityPostingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}