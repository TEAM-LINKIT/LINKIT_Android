package com.example.linkit_android.portfolio.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.linkit_android.databinding.ActivityIntroductionBinding
import com.example.linkit_android.databinding.ActivityLoginBinding
import com.example.linkit_android.model.PortfolioModel
import com.example.linkit_android.util.SharedPreferenceController
import com.google.firebase.database.*

class IntroductionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIntroductionBinding

    private val firebaseDatabase : FirebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference : DatabaseReference = firebaseDatabase.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroductionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initSaveBtn()
        pullIntroductionFromServer()
    }

    // 이미 저장된 값이 있는 경우
    private fun pullIntroductionFromServer() {
        val uid = SharedPreferenceController.getUid(this).toString()
        databaseReference.child("users").child(uid).child("portfolio").child("introduction")
                .addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            binding.etContentIntroduce.setText(snapshot.value.toString())
                            binding.etContentIntroduce.setSelection(binding.etContentIntroduce.length())
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {}
                })
    }

    private fun initSaveBtn() {
        binding.btnSave.setOnClickListener {
            pushIntroductionToServer()
            goToPortfolioFragment()
        }
    }

    private fun pushIntroductionToServer() {
        val uid = SharedPreferenceController.getUid(this).toString()
        val introContent = binding.etContentIntroduce.text.toString()
        // val portfolioModel = PortfolioModel(introContent,null,null,null,null)
        databaseReference.child("users").child(uid).child("portfolio").child("introduction")
                .setValue(introContent)
    }

    private fun goToPortfolioFragment() {
        var intent = Intent(this, PortfolioFragment::class.java)
        val introContent = binding.etContentIntroduce.text.toString()
        intent.putExtra("introductionContent", introContent)

        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}