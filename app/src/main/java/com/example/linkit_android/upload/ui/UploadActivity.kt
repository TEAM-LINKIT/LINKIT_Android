package com.example.linkit_android.upload.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.example.linkit_android.R
import com.example.linkit_android.databinding.ActivityUploadBinding
import com.example.linkit_android.model.PostingModel
import com.example.linkit_android.util.SharedPreferenceController
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.sql.Timestamp
import java.util.*

class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding

    private lateinit var uid: String

    private val currentDate = Calendar.getInstance()

    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference: DatabaseReference = firebaseDatabase.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setViewBinding()

        setPref()

        setCurrentDate()

        initStartDateBtn()

        initEndDateBtn()

        initPartCountText()

        initAddPartBtn()

        initUploadBtn()

        initBackBtn()
    }

    private fun setViewBinding() {
        binding = ActivityUploadBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    private fun setPref() {
        uid = SharedPreferenceController.getUid(this).toString()
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

    private fun TextView.setDateText(year: Int, month: Int) {
        this.text = getString(R.string.select_date_string, year, month)
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

    private fun initUploadBtn() {
        binding.btnUpload.setOnClickListener {
            if (binding.etTitle.text.isEmpty())
                Toast.makeText(this, "제목을 입력해주세요", Toast.LENGTH_SHORT).show()
            else if (binding.etContent.text.isEmpty())
                Toast.makeText(this, "내용을 입력해주세요", Toast.LENGTH_SHORT).show()
            else if (upload_plan_count == 0 && upload_design_count == 0 &&
                    upload_frontend_count == 0 && upload_backend_count == 0)
                Toast.makeText(this, "모집 인원을 설정해주세요", Toast.LENGTH_SHORT).show()
            else
                pushPostingToServer()
        }
    }

    private fun pushPostingToServer() {
        val timeStamp = Timestamp(System.currentTimeMillis()).time.toString()
        val postingModel = PostingModel()
        postingModel.apply {
            this.id = timeStamp
            this.title = binding.etTitle.text.toString()
            this.content = binding.etContent.text.toString()
            this.writer = uid
            this.startDate = getString(R.string.select_date_string, upload_start_year, upload_start_month)
            this.endDate = getString(R.string.select_date_string, upload_end_year, upload_end_month)
            this.recruitNum = mutableListOf(upload_plan_count, upload_design_count, upload_frontend_count, upload_backend_count)
        }
        databaseReference.child("community").child(timeStamp).setValue(postingModel).addOnSuccessListener {
            pushPostingToUserNode(timeStamp)
        }
    }

    private fun pushPostingToUserNode(key: String) {
        databaseReference.child("users").child(uid).child("posting").push().setValue(key)
                .addOnSuccessListener {
                    Toast.makeText(this, "정상적으로 글이 등록되었습니다.", Toast.LENGTH_SHORT).show()
                    finish()
                }
    }

    private fun initBackBtn() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

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