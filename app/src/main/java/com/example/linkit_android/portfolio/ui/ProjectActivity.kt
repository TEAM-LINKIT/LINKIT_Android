package com.example.linkit_android.portfolio.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.linkit_android.databinding.ActivityProjectBinding

class ProjectActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProjectBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}