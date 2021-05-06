package com.example.linkit_android.signup

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.linkit_android.R
import com.example.linkit_android.databinding.ActivitySignUpPartBinding
import com.example.linkit_android.login.LoginActivity
import com.example.linkit_android.model.UserModel
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import java.util.*

class SignUpPartActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpPartBinding

    private lateinit var id: String
    private lateinit var pwd: String
    private lateinit var name: String
    private lateinit var profileImg: String

    private var selectPart = -1
    private lateinit var pushToken: String

    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference: DatabaseReference = firebaseDatabase.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpPartBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        getIntentValue()

        initPartCheckBox()

        initFinishBtn()
    }

    private fun getIntentValue() {
        id = intent.getStringExtra("id").toString()
        pwd = intent.getStringExtra("pwd").toString()
        name = intent.getStringExtra("name").toString()
        profileImg = intent.getStringExtra("profileImg").toString()
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

    private fun initFinishBtn() {
        binding.btnFinish.setOnClickListener {
            if (selectPart != -1) {
                getPushToken()
                pushUserAuthenticationToServer()
                goToLoginActivity()
            } else {
                Toast.makeText(this, "관심 파트를 선택해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getPushToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (!it.isSuccessful)
                return@addOnCompleteListener
            pushToken = it.result.toString()
        }
    }

    private fun pushUserAuthenticationToServer() {
        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(id, pwd)
            .addOnCompleteListener {
            val uid = it.result?.user?.uid!!
            val userModel = UserModel(uid, id, name, selectPart, profileImg, pushToken)
            databaseReference.child("users").child(uid).setValue(userModel)
        }
    }

    private fun goToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.apply {
            putExtra("id", id)
            putExtra("pwd", pwd)
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}