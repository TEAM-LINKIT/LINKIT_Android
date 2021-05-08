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

        initPartCountText()

        initAddPartBtn()
    }

    private fun setViewBinding() {
        binding = ActivityUploadBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    /* 프로젝트 기간 설정 */

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

    /* 모집 파트 및 인원 설정 */

    private fun initPartCountText() {
        upload_plan_count = 0
        upload_design_count = 0
        upload_frontend_count = 0
        upload_backend_count = 0
        setPartCountText()
    }

    private fun initAddPartBtn() {
        binding.btnAdd.setOnClickListener {
            val selectPartDialog = SelectPartDialogFragment {
                upload_plan_count = it[0]
                upload_design_count = it[1]
                upload_frontend_count = it[2]
                upload_backend_count = it[3]
                setPartCountText()
            }
            selectPartDialog.show(supportFragmentManager, "select_part_dialog")
        }
    }

    private fun setPartCountText() {
        binding.apply {
            tvCountPlan.text = getString(R.string.part_count_string, upload_plan_count)
            tvCountDesign.text = getString(R.string.part_count_string, upload_design_count)
            tvCountFrontend.text = getString(R.string.part_count_string, upload_frontend_count)
            tvCountBackend.text = getString(R.string.part_count_string, upload_backend_count)
        }
    }

    // Todo: 업로드 버튼 클릭 시 firebase로 전송 -> 시작일과 종료일은 "yyyy년 mm월" string 형식으로 저장하기
    // Todo: 파트 별 모집 인원 firebase로 보낼 때는 upload_... 값 보내기

    companion object {
        var upload_start_year = 0
        var upload_start_month = 0
        var upload_end_year = 0
        var upload_end_month = 0
        var upload_start_flag = false
        var upload_end_flag = false
        var upload_plan_count = 0
        var upload_design_count = 0
        var upload_frontend_count = 0
        var upload_backend_count = 0
    }
}