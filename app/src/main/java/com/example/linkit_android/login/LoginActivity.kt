package com.example.linkit_android.login

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.linkit_android.databinding.ActivityLoginBinding
import com.example.linkit_android.main.ui.MainActivity
import com.example.linkit_android.signup.SignUpActivity
import com.example.linkit_android.util.SharedPreferenceController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var uid: String
    private lateinit var id: String
    private lateinit var name: String
    private var part: Int = -1
    private lateinit var profileImg: String
    private lateinit var pushToken: String

    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference: DatabaseReference = firebaseDatabase.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        autoLogin()

        initLoginBtn()

        initSignUpBtn()
    }

    /* 로그인 */

    private fun initLoginBtn() {
        binding.btnLogin.setOnClickListener {
            val id = binding.etId.text.toString()
            val pwd = binding.etPwd.text.toString()
            if (id.isNotEmpty() && pwd.isNotEmpty()) {
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(id, pwd)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            uid = it.result?.user?.uid!!
                            getUserInfo()
                            goToMainActivity()
                        }  else {
                            Toast.makeText(this, "가입되지 않은 회원입니다", Toast.LENGTH_SHORT).show()
                            Log.d("LoginActivity", it.exception?.message!!)
                        }
                    }
            } else
                Toast.makeText(this, "아이디 및 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getUserInfo() {
        databaseReference.child("users").child(uid)
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists()) {
                        Toast.makeText(this@LoginActivity, "가입되지 않은 회원입니다", Toast.LENGTH_SHORT).show()
                    } else {
                        id = snapshot.child("id").value.toString()
                        name = snapshot.child("userName").value.toString()
                        part = snapshot.child("userPart").value.toString().toInt()
                        profileImg = snapshot.child("profileImg").value.toString()
                        pushToken = snapshot.child("pushToken").value.toString()

                        setPref()
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun setPref() {
        SharedPreferenceController.apply {
            setUid(applicationContext, uid)
            setId(applicationContext, id)
            setUserName(applicationContext, name)
            setUserPart(applicationContext, part)
            setProfileImg(applicationContext, profileImg)
            setPushToken(applicationContext, pushToken)
        }
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun autoLogin() {
        SharedPreferenceController.apply {
            if (!getUid(this@LoginActivity).isNullOrBlank()) {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    /* 회원가입 */

    private fun initSignUpBtn() {
        binding.btnSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivityForResult(intent, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                binding.etId.setText(data!!.getStringExtra("id"))
                binding.etPwd.setText(data.getStringExtra("pwd"))
            }
        }
    }
}