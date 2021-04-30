package com.example.linkit_android.profile.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.linkit_android.databinding.ActivityRecommendBinding

class RecommendActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecommendBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setViewBinding()
    }

    private fun setViewBinding() {
        binding = ActivityRecommendBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}