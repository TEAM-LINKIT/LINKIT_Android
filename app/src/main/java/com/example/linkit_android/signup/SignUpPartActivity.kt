package com.example.linkit_android.signup

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.linkit_android.R
import com.example.linkit_android.databinding.ActivitySignUpPartBinding
import com.google.android.material.card.MaterialCardView

class SignUpPartActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpPartBinding

    private lateinit var id: String
    private lateinit var pwd: String
    private lateinit var name: String
    private lateinit var profileUri: Uri

    private var selectPart = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpPartBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        getIntentValue()

        initPartCheckBox()
    }

    private fun getIntentValue() {
        id = intent.getStringExtra("id").toString()
        pwd = intent.getStringExtra("pwd").toString()
        name = intent.getStringExtra("name").toString()
        profileUri = intent.getParcelableExtra("profileImg")!!
    }

    /* 파트 선택 체크박스 관련 함수 */

    private fun initPartCheckBox() {
        binding.apply {
            btnPlan.setClickListenerOnCheckbox()
            btnDesign.setClickListenerOnCheckbox()
            btnFrontDev.setClickListenerOnCheckbox()
            btnBackDev.setClickListenerOnCheckbox()
        }
    }

    private fun MaterialCardView.setClickListenerOnCheckbox() {
        this.setOnClickListener {
            if (!this.isChecked) {
                disableAllPartCheckbox()
                this.isChecked = true
                this.setCheckedState(true)
                setSelectPartValue(this.id)
            } else {
                this.isChecked = false
                this.setCheckedState(false)
                selectPart = -1
            }
        }
    }

    private fun MaterialCardView.setCheckedState(isChecked: Boolean) {
        if (isChecked) {
            this.apply {
                strokeColor = ContextCompat.getColor(this@SignUpPartActivity, R.color.red_main)
                invalidate()
                setTextViewColorRed()
            }
        } else {
            this.apply {
                strokeColor = ContextCompat.getColor(this@SignUpPartActivity, R.color.white)
                invalidate()
                setTextViewColorGray()
            }
        }
    }

    private fun MaterialCardView.setTextViewColorRed() {
        binding.apply {
            when (this@setTextViewColorRed.id) {
                btnPlan.id -> tvPlan.setTextColor(ContextCompat.getColor(this@SignUpPartActivity, R.color.red_main))
                btnDesign.id -> tvDesign.setTextColor(ContextCompat.getColor(this@SignUpPartActivity, R.color.red_main))
                btnFrontDev.id -> tvFrontDev.setTextColor(ContextCompat.getColor(this@SignUpPartActivity, R.color.red_main))
                btnBackDev.id -> tvBackDev.setTextColor(ContextCompat.getColor(this@SignUpPartActivity, R.color.red_main))
            }
        }
    }

    private fun MaterialCardView.setTextViewColorGray() {
        binding.apply {
            when (this@setTextViewColorGray.id) {
                btnPlan.id -> tvPlan.setTextColor(ContextCompat.getColor(this@SignUpPartActivity, R.color.gray_content))
                btnDesign.id -> tvDesign.setTextColor(ContextCompat.getColor(this@SignUpPartActivity, R.color.gray_content))
                btnFrontDev.id -> tvFrontDev.setTextColor(ContextCompat.getColor(this@SignUpPartActivity, R.color.gray_content))
                btnBackDev.id -> tvBackDev.setTextColor(ContextCompat.getColor(this@SignUpPartActivity, R.color.gray_content))
            }
        }
    }

    private fun setSelectPartValue(id: Int) {
        binding.apply {
            when (id) {
                btnPlan.id -> selectPart = 0
                btnDesign.id -> selectPart = 1
                btnFrontDev.id -> selectPart = 2
                btnBackDev.id -> selectPart = 3
            }
        }
    }

    private fun disableAllPartCheckbox() {
        binding.apply {
            btnPlan.isChecked = false
            btnDesign.isChecked = false
            btnFrontDev.isChecked = false
            btnBackDev.isChecked = false
            btnPlan.setCheckedState(false)
            btnDesign.setCheckedState(false)
            btnFrontDev.setCheckedState(false)
            btnBackDev.setCheckedState(false)
        }
    }

    // Todo: selectPart가 -1이면 파트를 선택해달라는 토스트 문구 등장, -1이 아니면 회원가입 완료 -> 로그인 뷰로 이동
}