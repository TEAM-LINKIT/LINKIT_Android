package com.example.linkit_android.upload.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.linkit_android.community.ui.CommunityFragment
import com.example.linkit_android.databinding.ActivityPostingBinding
import com.example.linkit_android.profile.ui.ProfileActivity
import com.example.linkit_android.util.SharedPreferenceController
import com.example.linkit_android.util.getPartString
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_posting.*

class PostingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostingBinding

    private val firebaseDatabase : FirebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference : DatabaseReference = firebaseDatabase.reference

    private lateinit var uid: String

    private lateinit var postingId : String
    private lateinit var writerId : String
    private lateinit var destPushToken: String
    private lateinit var postingTitle: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        postingId = intent.getStringExtra("postingId").toString()

        setViewBinding()

        setPref()

        initApplyBtn()

        setPost()

        initBackBtn()

        initWriterProfileBtn()
    }

    private fun setViewBinding() {
        binding = ActivityPostingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    private fun setPref() {
        uid = SharedPreferenceController.getUid(this).toString()
    }

    private fun initApplyBtn() {
        binding.btnApply.setOnClickListener {
            if (uid == writerId) {
                Toast.makeText(this, "자신이 작성한 글에는 지원할 수 없습니다", Toast.LENGTH_SHORT).show()
            } else {
                val sendPortfolioDialog = SendPortfolioDialogFragment()
                val args = Bundle()
                val array = arrayListOf(writerId, destPushToken, postingId, postingTitle)
                args.putStringArrayList("destUserInfo", array)
                sendPortfolioDialog.arguments = args
                sendPortfolioDialog.show(supportFragmentManager, "send_portfolio_dialog")
            }
        }
    }

    private fun initBackBtn() {
        binding.btnBack.setOnClickListener {
            val intent = Intent(this, CommunityFragment::class.java)
            setResult(Activity.RESULT_CANCELED, intent)
            finish()
        }
    }

    private fun initWriterProfileBtn() {
        binding.imgWriterProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("writerId", writerId)
            startActivity(intent)
        }
    }

    private fun setPost() {
        databaseReference.child("community").child(postingId)
                .addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        writerId =  snapshot.child("writer").value.toString()
                        setWriter(writerId)

                        binding.tvTitle.text = snapshot.child("title").value.toString()
                        binding.tvDate.text = snapshot.child("startDate").value.toString() + " ㅡ " + snapshot.child("endDate").value.toString()
                        binding.tvCountPlan.text = snapshot.child("recruitNum").child("0").value.toString() + "명"
                        binding.tvCountDesign.text = snapshot.child("recruitNum").child("1").value.toString() + "명"
                        binding.tvCountFrontend.text = snapshot.child("recruitNum").child("2").value.toString() + "명"
                        binding.tvCountBackend.text = snapshot.child("recruitNum").child("3").value.toString() + "명"
                        binding.tvContent.text = snapshot.child("content").value.toString()

                        postingTitle = snapshot.child("title").value.toString()
                    }
                    override fun onCancelled(error: DatabaseError) {}
                })
    }

    private fun setWriter(writerId : String) {
        databaseReference.child("users").child(writerId)
                .addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        binding.tvWriterName.text = snapshot.child("userName").value.toString()
                        Glide.with(this@PostingActivity).load(snapshot.child("profileImg").value.toString()).into(binding.imgWriterProfile)
                        binding.tvWriterPart.text = getPartString(snapshot.child("userPart").value.toString().toInt())
                        destPushToken = snapshot.child("pushToken").value.toString()
                    }
                    override fun onCancelled(error: DatabaseError) {}
                })
    }
}