package com.example.linkit_android.upload.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.linkit_android.databinding.ActivityPostingBinding

class PostingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setViewBinding()

        initApplyBtn()
    }

    private fun setViewBinding() {
        binding = ActivityPostingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    private fun initApplyBtn() {
        binding.btnApply.setOnClickListener {
            val sendPortfolioDialog = SendPortfolioDialogFragment()
            sendPortfolioDialog.show(supportFragmentManager, "send_portfolio_dialog")
        }
    }
}