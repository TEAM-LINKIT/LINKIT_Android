package com.example.linkit_android.portfolio.ui

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
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.linkit_android.databinding.ActivityProjectBinding
import com.example.linkit_android.util.SharedPreferenceController
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.item_project.*
import java.text.SimpleDateFormat
import java.util.*

class ProjectActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProjectBinding

    val STORAGE_PERMISSION = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    val FLAG_PERM_STORAGE = 99
    val FLAG_REQ_STORAGE = 102

    private lateinit var projectUri: Uri
    private lateinit var projectImg: String

    private val firebaseStorage = FirebaseStorage.getInstance()
    private val firebaseDatabase : FirebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference : DatabaseReference = firebaseDatabase.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initSaveBtn()

        initProjectImgBtn()

    }

    /* project link 제외하고 누락된 내용이 있으면 알림을 띄운다 */

    private fun initSaveBtn() {
        binding.btnSave.setOnClickListener {
            if(binding.etProjectName.text.toString().isEmpty() || binding.etContentProject.text.toString().isEmpty() ||
                    !this::projectImg.isInitialized) {
                Toast.makeText(this, "모든 항목을 입력해 주세요", Toast.LENGTH_SHORT).show()
            }

            else {
                pushProjectToServer()
                goToPortfolioFragment()
            }
        }
    }

    /* 갤러리에서 사진 가져오기 */

    private fun initProjectImgBtn() {
        binding.imgProject.setOnClickListener {
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
                projectUri = uri!!
                setProjectImageView(uri)
                uploadImgToStorage()
            }
        }
    }

    private fun setProjectImageView(uri: Uri?) {
        binding.sampleImg.visibility = View.GONE
        binding.imgProjectSelect.apply {
            visibility = View.VISIBLE
            setImageURI(uri)
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun uploadImgToStorage() {
        val simpleDateForm = SimpleDateFormat("yyyyMMddhhmmss")
        val fileName = simpleDateForm.format(Date()) + ".png"
        val imgRef = firebaseStorage.getReference("projectImages/$fileName")
        imgRef.putFile(projectUri).addOnSuccessListener {
            imgRef.downloadUrl.addOnSuccessListener {
                projectImg = it.toString()
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
        if (grantResults.isNotEmpty()
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGallery()
        } else {
            Toast.makeText(this, "저장소 권한을 승인해야 프로필 이미지를 설정할 수 있습니다",
                    Toast.LENGTH_SHORT).show()
            return
        }
    }

    private fun pushProjectToServer() {
        val simpleDateForm = SimpleDateFormat("yyyyMMddhhmmss")
        val projectName = simpleDateForm.format(Date())
        val uid = SharedPreferenceController.getUid(this).toString()
        var projectList = mutableListOf<String>(projectImg, binding.etProjectName.text.toString(),
                binding.etContentProject.text.toString(), binding.etProjectLink.text.toString())
        databaseReference.child("users").child(uid).child("portfolio").child("project")
                .child(projectName).setValue(projectList)
    }

    private fun goToPortfolioFragment() {
        var intent = Intent(this, PortfolioFragment::class.java)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }


}