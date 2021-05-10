package com.example.linkit_android.portfolio.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import com.example.linkit_android.databinding.ActivityEducationBinding
import com.example.linkit_android.model.PortfolioModel
import com.example.linkit_android.util.SharedPreferenceController
import com.google.firebase.database.*

class EducationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEducationBinding

    private val firebaseDatabase : FirebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference : DatabaseReference = firebaseDatabase.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEducationBinding.inflate(layoutInflater)
        var view = binding.root
        setContentView(view)
        pullEducationFromServer()
        initSaveBtn()
    }

    private fun pullEducationFromServer() {
        val uid = SharedPreferenceController.getUid(this).toString()
        databaseReference.child("users").child(uid).child("portfolio").child("education")
                .addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            binding.etSchool.setText(snapshot.child("0").value.toString())
                            binding.etSchool.setSelection(binding.etSchool.length())
                            binding.etDepartment.setText(snapshot.child("1").value.toString())
                            binding.etDepartment.setSelection(binding.etDepartment.length())
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {}
                })
    }

    private fun initSaveBtn() {
        binding.btnSave.setOnClickListener {
            if(binding.etSchool.text.toString()=="" && binding.etDepartment.text.toString()!="") {
                Toast.makeText(this, "학교를 입력해주세요", Toast.LENGTH_SHORT).show()
            }

            else {
                pushEducationToServer()
                goToPortfolioFragment()
            }
        }
    }

    private fun pushEducationToServer() {
        val uid = SharedPreferenceController.getUid(this).toString()
        var educationList = mutableListOf<String>(binding.etSchool.text.toString(), binding.etDepartment.text.toString())
        databaseReference.child("users").child(uid).child("portfolio").child("education")
                .setValue(educationList)
    }

    private fun goToPortfolioFragment() {
        var intent = Intent(this, PortfolioFragment::class.java)
        val educationList = arrayListOf<String>(binding.etSchool.text.toString(), binding.etDepartment.text.toString())
        intent.putExtra("educationContentList", educationList)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}