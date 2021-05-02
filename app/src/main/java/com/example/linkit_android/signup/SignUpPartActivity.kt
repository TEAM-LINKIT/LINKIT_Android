package com.example.linkit_android.signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.linkit_android.databinding.ActivitySignUpPartBinding

class SignUpPartActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpPartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpPartBinding.inflate(layoutInflater)
        var view = binding.root
        setContentView(view)
    }
}