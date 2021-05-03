package com.example.linkit_android.portfolio.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.linkit_android.databinding.ActivityIntroductionBinding
import com.example.linkit_android.databinding.ActivityLoginBinding

class IntroductionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIntroductionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroductionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}