package com.example.linkit_android.portfolio.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.linkit_android.databinding.ActivityEducationBinding

class EducationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEducationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEducationBinding.inflate(layoutInflater)
        var view = binding.root
        setContentView(view)
    }
}