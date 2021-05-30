package com.example.linkit_android.signup

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.linkit_android.R
import com.example.linkit_android.databinding.ActivitySignUpProfileBinding
import com.example.linkit_android.util.hideKeyboard
import com.example.linkit_android.util.showKeyboard
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

class SignUpProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpProfileBinding

    val STORAGE_PERMISSION = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    val FLAG_PERM_STORAGE = 99
    val FLAG_REQ_STORAGE = 102

    private lateinit var id: String
    private lateinit var pwd: String
    private lateinit var profileUri: Uri
    private lateinit var profileImg: String

    private val firebaseStorage = FirebaseStorage.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        getIntentValue()

        setTouchListenerOnEditText()

        setKeyListenerOnEditText()

        initCameraBtn()

        initNextBtn()
    }

    private fun getIntentValue() {
        id = intent.getStringExtra("id").toString()
        pwd = intent.getStringExtra("pwd").toString()
    }

    /* EditText 관련 함수 */

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchListenerOnEditText() {
        binding.etName.setOnTouchListener { _, _ ->
            setEditTextEnabled()
            showKeyboard(this)
            true
        }
    }

    private fun setEditTextEnabled() {
        binding.etName.apply {
            requestFocus()
            isFocusable = true
            isCursorVisible = true
            setSelection(this.length())
        }
        binding.dividerName.setDividerBgColor(true)
    }

    private fun setKeyListenerOnEditText() {
        binding.etName.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KEYCODE_ENTER || keyCode == EditorInfo.IME_ACTION_DONE) {
                setEditTextDisabled()
                hideKeyboard(this, binding.etName)
                return@setOnKeyListener true
            }
            false
        }
    }

    private fun setEditTextDisabled() {
        binding.etName.apply {
            clearFocus()
            isCursorVisible = false
        }
        binding.dividerName.setDividerBgColor(false)
    }

    private fun View.setDividerBgColor(isRed: Boolean) {
        if (isRed)
            this.setBackgroundColor(ContextCompat.getColor(this@SignUpProfileActivity, R.color.red_main))
        else
            this.setBackgroundColor(ContextCompat.getColor(this@SignUpProfileActivity, R.color.gray_underline))
    }

    /* 갤러리에서 사진 가져오기 */

    private fun initCameraBtn() {
        binding.btnPic.setOnClickListener {
            if (checkPermission(STORAGE_PERMISSION, FLAG_PERM_STORAGE))
                openGallery()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, FLAG_REQ_STORAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == FLAG_REQ_STORAGE) {
                val uri = data?.data
                profileUri = uri!!
                setProfileImageView(uri)
                uploadImgToStorage()
            }
        }
    }

    private fun setProfileImageView(uri: Uri?) {
        binding.imgExample.visibility = View.INVISIBLE
        binding.imgProfileSelect.apply {
            visibility = View.VISIBLE
            setImageURI(uri)
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun uploadImgToStorage() {
        val simpleDateForm = SimpleDateFormat("yyyyMMddhhmmss")
        val fileName = simpleDateForm.format(Date()) + ".png"
        val imgRef = firebaseStorage.getReference("profileImages/$fileName")
        imgRef.putFile(profileUri).addOnSuccessListener {
            imgRef.downloadUrl.addOnSuccessListener {
                profileImg = it.toString()
            }
        }
    }

    /* 갤러리 접근 권한 설정 */

    private fun checkPermission(permissions: Array<out String>, flag: Int) : Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, permissions, flag)
                    return false
                }
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGallery()
        } else {
            Toast.makeText(this, "저장소 권한을 승인해야 프로필 이미지를 설정할 수 있습니다",
                Toast.LENGTH_SHORT).show()
            return
        }
    }

    /* 다음 버튼 클릭 시 동작하는 함수 */

    private fun initNextBtn() {
        binding.btnNext.setOnClickListener {
            if (binding.etName.text.toString().isNotEmpty())
                goToSignUpPartActivity()
            else
                Toast.makeText(this, "이름을 입력해주세요", Toast.LENGTH_SHORT).show()
        }
    }

    private fun goToSignUpPartActivity() {
        val intent = Intent(this, SignUpPartActivity::class.java)
        intent.apply {
            putExtra("id", id)
            putExtra("pwd", pwd)
            putExtra("name", binding.etName.text.toString())
            putExtra("profileImg", profileImg)
            addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT)
        }
        startActivity(intent)
        finish()
    }
}