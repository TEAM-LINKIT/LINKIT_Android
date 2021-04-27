package com.example.linkit_android.upload.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.linkit_android.databinding.ActivityUploadBinding

class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setViewBinding()

        initStartDateBtn()

        initEndDateBtn()

        initAddPartBtn()
    }

    private fun setViewBinding() {
        binding = ActivityUploadBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    private fun initStartDateBtn() {
        binding.btnStartDate.setOnClickListener {
            showSettingDateDialog()
        }
    }

    private fun initEndDateBtn() {
        binding.btnEndDate.setOnClickListener {
            showSettingDateDialog()
        }
    }

    private fun showSettingDateDialog() {
        val settingDateDialog = SettingDateDialogFragment()
        settingDateDialog.show(supportFragmentManager, "setting_date_dialog")
    }

    private fun initAddPartBtn() {
        binding.btnAdd.setOnClickListener {
            val selectPartDialog = SelectPartDialogFragment()
            selectPartDialog.show(supportFragmentManager, "select_part_dialog")
        }
    }
}