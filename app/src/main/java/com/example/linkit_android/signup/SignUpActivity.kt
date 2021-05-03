package com.example.linkit_android.signup

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.linkit_android.R
import com.example.linkit_android.databinding.ActivitySignUpBinding
import com.example.linkit_android.util.hideKeyboard
import com.example.linkit_android.util.showKeyboard

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setListenerOnEditText()

        initNextBtn()
    }

    /* EditText 관련 함수 */

    private fun setListenerOnEditText() {
        binding.apply {
            etId.setTouchListener()
            etPwd.setTouchListener()
            etPwd2.setTouchListener()
            etId.setKeyListener()
            etPwd.setKeyListener()
            etPwd2.setKeyListener()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun EditText.setTouchListener() {
        this.setOnTouchListener { _, _ ->
            setAllEditTextDisabled()
            setEditTextEnabled(this)
            showKeyboard(this@SignUpActivity)
            true
        }
    }

    private fun setAllEditTextDisabled() {
        binding.apply {
            setEditTextDisabled(etId)
            setEditTextDisabled(etPwd)
            setEditTextDisabled(etPwd2)
        }
    }

    private fun setEditTextEnabled(editText: EditText) {
        editText.apply {
            requestFocus()
            isFocusable = true
            isCursorVisible = true
            setSelection(this.length())
            setDividerEnabled()
        }
    }

    private fun EditText.setDividerEnabled() {
        binding.apply {
            when (this@setDividerEnabled.id) {
                etId.id -> dividerId.setDividerBgColor(true)
                etPwd.id -> dividerPwd.setDividerBgColor(true)
                etPwd2.id -> dividerPwd2.setDividerBgColor(true)
            }
        }
    }

    private fun EditText.setKeyListener() {
        this.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KEYCODE_ENTER || keyCode == EditorInfo.IME_ACTION_DONE) {
                setEditTextDisabled(this)
                hideKeyboard(this@SignUpActivity, this)
                return@setOnKeyListener true
            }
            false
        }
    }

    private fun setEditTextDisabled(editText: EditText) {
        editText.apply {
            clearFocus()
            isCursorVisible = false
            setDividerDisabled()
        }
    }

    private fun EditText.setDividerDisabled() {
        binding.apply {
            when (this@setDividerDisabled.id) {
                etId.id -> dividerId.setDividerBgColor(false)
                etPwd.id -> dividerPwd.setDividerBgColor(false)
                etPwd2.id -> dividerPwd2.setDividerBgColor(false)
            }
        }
    }

    private fun View.setDividerBgColor(isRed: Boolean) {
        if (isRed)
            this.setBackgroundColor(ContextCompat.getColor(this@SignUpActivity, R.color.red_main))
        else
            this.setBackgroundColor(ContextCompat.getColor(this@SignUpActivity, R.color.gray_underline))
    }

    /* 다음 버튼 클릭 시 동작하는 함수 */

    private fun initNextBtn() {
        binding.btnNext.setOnClickListener {
            binding.apply {
                if (etId.text.toString().isNotEmpty() &&
                    etPwd.text.toString().isNotEmpty() &&
                    etPwd2.text.toString().isNotEmpty()) {
                    if (etPwd.text.toString() == etPwd2.text.toString())
                            goToSignUpProfileActivity()
                    else
                        Toast.makeText(this@SignUpActivity, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
                }
                else
                    Toast.makeText(this@SignUpActivity, "빈칸을 채워주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun goToSignUpProfileActivity() {
        val intent = Intent(this, SignUpProfileActivity::class.java)
        intent.putExtra("id", binding.etId.text.toString())
        intent.putExtra("pwd", binding.etPwd.text.toString())
        startActivity(intent)
        finish()
    }
}