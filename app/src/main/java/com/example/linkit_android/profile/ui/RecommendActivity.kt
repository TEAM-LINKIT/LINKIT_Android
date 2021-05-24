package com.example.linkit_android.profile.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.linkit_android.databinding.ActivityRecommendBinding
import com.example.linkit_android.portfolio.adapter.ProjectData
import com.example.linkit_android.portfolio.ui.PortfolioFragment
import com.example.linkit_android.util.SharedPreferenceController
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.schedule

class RecommendActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecommendBinding

    private val firebaseDatabase : FirebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference : DatabaseReference = firebaseDatabase.reference

    private lateinit var writerId : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var intent = getIntent()
        writerId = intent.getStringExtra("writerId").toString()

        setViewBinding()

        initBackBtn()

        initSaveBtn()
    }

    private fun setViewBinding() {
        binding = ActivityRecommendBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    private fun initBackBtn() {
        binding.btnBack.setOnClickListener {
            var intent = Intent(this, ProfileActivity::class.java)
            setResult(Activity.RESULT_CANCELED, intent)
            finish()
        }
    }

    private fun initSaveBtn() {
        binding.btnWrite.setOnClickListener {
            if(binding.etContentRecommend.text.toString().isEmpty()) {
                Toast.makeText(this, "추천사를 입력해 주세요", Toast.LENGTH_SHORT).show()
            }

            else {
                pushRecommendToServer()
            }
        }
    }

    private fun pushRecommendToServer() {
        val simpleDateForm = SimpleDateFormat("yyyyMMddhhmmss")
        val recommendName = simpleDateForm.format(Date())
        val uid = SharedPreferenceController.getUid(this).toString()
        lateinit var recommenddata : ProjectData
        databaseReference.child("users").addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        recommenddata = ProjectData(snapshot.child(uid).child("profileImg").value.toString(),
                                snapshot.child(uid).child("userName").value.toString(),
                                binding.etContentRecommend.text.toString(), "")

                        databaseReference.child("users").child(writerId).child("recommend")
                                .child(recommendName).setValue(recommenddata)

                        goToProfileActivity()

                    }
                    override fun onCancelled(error: DatabaseError) {}
                })
    }

    private fun goToProfileActivity() {
        var intent = Intent(this, ProfileActivity::class.java)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}