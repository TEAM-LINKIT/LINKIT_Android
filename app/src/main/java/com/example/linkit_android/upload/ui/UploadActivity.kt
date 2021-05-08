package com.example.linkit_android.upload.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.linkit_android.R
import com.example.linkit_android.databinding.ActivityUploadBinding
import java.util.*

class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding

    private val currentDate = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setViewBinding()

        setCurrentDate()

        initStartDateBtn()

        initEndDateBtn()

        initAddPartBtn()
    }

    private fun setViewBinding() {
        binding = ActivityUploadBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    private fun setCurrentDate() {
        upload_start_year = currentDate.get(Calendar.YEAR)
        upload_start_month = currentDate.get(Calendar.MONTH) + 1
        binding.tvStartDate.setDateText(upload_start_year, upload_start_month)

        upload_end_year = currentDate.get(Calendar.YEAR)
        upload_end_month = currentDate.get(Calendar.MONTH) + 1
        binding.tvEndDate.setDateText(upload_end_year, upload_end_month)
    }

    private fun initStartDateBtn() {
        binding.btnStartDate.setOnClickListener {
            showSettingStartDateDialog()
        }
    }

    private fun initEndDateBtn() {
        binding.btnEndDate.setOnClickListener {
            showSettingEndDateDialog()
        }
    }

    private fun showSettingStartDateDialog() {
        upload_start_flag = true
        val settingDateDialog = SettingDateDialogFragment {
            upload_start_year = it[0]
            upload_start_month = it[1]
            binding.tvStartDate.setDateText(it[0], it[1])
        }
        settingDateDialog.show(supportFragmentManager, "setting_start_date_dialog")
    }

    private fun showSettingEndDateDialog() {
        upload_end_flag = true
        val settingDateDialog = SettingDateDialogFragment {
            upload_end_year = it[0]
            upload_end_month = it[1]
            binding.tvEndDate.setDateText(it[0], it[1])
        }
        settingDateDialog.show(supportFragmentManager, "setting_end_date_dialog")
    }

    private fun TextView.setDateText(startDate: Int, endDate: Int) {
        this.text = getString(R.string.select_date_string, startDate, endDate)
    }

    private fun initAddPartBtn() {
        binding.btnAdd.setOnClickListener {
            val selectPartDialog = SelectPartDialogFragment()
            selectPartDialog.show(supportFragmentManager, "select_part_dialog")
        }
    }

    // Todo: 업로드 버튼 클릭 시 firebase로 전송 -> 시작일과 종료일은 "yyyy년 mm월" string 형식으로 저장하기

    companion object {
        var upload_start_year = 0
        var upload_start_month = 0
        var upload_end_year = 0
        var upload_end_month = 0
        var upload_start_flag = false
        var upload_end_flag = false
    }
}