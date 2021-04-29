package com.example.linkit_android.signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.linkit_android.databinding.ActivitySignUpBinding
import com.example.linkit_android.databinding.ActivitySignUpProfileBinding

class SignUpProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpProfileBinding.inflate(layoutInflater)
        var view = binding.root
        setContentView(view)
    }
}