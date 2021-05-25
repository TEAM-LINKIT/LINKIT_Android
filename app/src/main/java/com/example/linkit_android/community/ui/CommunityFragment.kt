package com.example.linkit_android.community.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.linkit_android.R
import com.example.linkit_android.community.adapter.CommunityAdapter
import com.example.linkit_android.community.adapter.CommunityData
import com.example.linkit_android.databinding.FragmentCommunityBinding
import com.example.linkit_android.model.PostingModel
import com.example.linkit_android.notification.ui.NotificationActivity
import com.example.linkit_android.upload.ui.PostingActivity
import com.example.linkit_android.upload.ui.UploadActivity
import com.example.linkit_android.util.ItemClickListener
import com.example.linkit_android.util.SharedPreferenceController
import com.google.firebase.database.*

class CommunityFragment : Fragment() {

    private var _binding: FragmentCommunityBinding? = null
    private val binding get() = _binding!!

    private lateinit var communityAdapter: CommunityAdapter
    private var postingList: MutableList<PostingModel> = mutableListOf()

    private var selectPart = -1

    private lateinit var uid: String
    private lateinit var userName: String
    private lateinit var profileImg: String
    private var userPart = -1

    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference: DatabaseReference = firebaseDatabase.reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommunityBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setPref()

        initProfile()

        checkNotification()

        initPartSpinner()

        initRecyclerView()

        goToUploadActivity()
    }

    private fun setPref() {
        SharedPreferenceController.apply {
            uid = getUid(requireContext()).toString()
            userName = getUserName(requireContext()).toString()
            profileImg = getProfileImg(requireContext()).toString()
            userPart = getUserPart(requireContext())
        }
    }

    private fun initProfile() {
        binding.apply {
            tvUserName.text = userName
            Glide.with(requireContext()).load(profileImg).into(imgProfile)
        }
    }

    private fun checkNotification() {
        databaseReference.child("users").child(uid).child("notification")
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists()) {
                        binding.constraintlayoutNone.visibility = View.VISIBLE
                        binding.constraintLayoutExist.visibility = View.GONE
                        initBannerUploadBtn()
                    } else {
                        for ((count, item) in snapshot.children.withIndex()) {
                            if (count == snapshot.childrenCount.toInt() - 1) {
                                val fromUid = item.child("uid").value.toString()
                                databaseReference.child("users").child(fromUid)
                                    .addListenerForSingleValueEvent(object: ValueEventListener {
                                        @SuppressLint("SetTextI18n")
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            val name = snapshot.child("userName").value.toString()
                                            val profileImg = snapshot.child("profileImg").value.toString()
                                            binding.tvCheckPortfolio.text = "${name}님의 제안을 확인해보세요!"
                                            Glide.with(requireContext()).load(profileImg)
                                                .into(binding.circleImageViewNotificationProfile)
                                        }
                                        override fun onCancelled(error: DatabaseError) {}
                                    })
                            }
                        }
                        binding.constraintLayoutExist.visibility = View.VISIBLE
                        binding.constraintlayoutNone.visibility = View.GONE
                        initBannerNotificationBtn()
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun initBannerUploadBtn() {
        binding.constraintlayoutNone.setOnClickListener {
            val intent = Intent(requireContext(), UploadActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initBannerNotificationBtn() {
        binding.constraintLayoutExist.setOnClickListener {
            val intent = Intent(requireContext(), NotificationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initPartSpinner() {
        val item = resources.getStringArray(R.array.part_array)
        val partAdapter = ArrayAdapter(requireContext(), R.layout.item_part_spinner, item)
        partAdapter.setDropDownViewResource(R.layout.item_part_spinner_content)

        binding.spinnerPart.apply {
            adapter = partAdapter
            setSelection(userPart)
        }
        setSelectedListenerOnSpinner()
    }

    private fun setSelectedListenerOnSpinner() {
        binding.spinnerPart.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> getPostingData(position)
                    1 -> getPostingData(position)
                    2 -> getPostingData(position)
                    3 -> getPostingData(position)
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun initRecyclerView() {
        communityAdapter = CommunityAdapter(requireContext())
        binding.recyclerviewCommunity.apply {
            adapter = communityAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun getPostingData(position: Int) {
        selectPart = position
        databaseReference.child("community").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                postingList.clear()
                for (data in snapshot.children) {
                    val postingData = data.getValue(PostingModel::class.java)
                    if (postingData?.recruitNum!![selectPart] != 0)
                        postingList.add(postingData)
                }
                bindData()
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun bindData() {
        communityAdapter.data = mutableListOf()
        for (data in postingList) {
            communityAdapter.data.add(CommunityData(data.title!!, setPartListData(data.recruitNum!!)))
        }
        communityAdapter.notifyDataSetChanged()

        initItemClickListener()
    }

    private fun setPartListData(partList: MutableList<Int>) : CharSequence {
        var partString: CharSequence = ""
        for (i in 0 until partList.size) {
            if (partList[i] != 0) {
                when (i) {
                    0 -> partString = TextUtils.concat(partString, setTextColor("기획", 0))
                    1 -> partString = TextUtils.concat(partString, setTextColor("디자인", 1))
                    2 -> partString = TextUtils.concat(partString, setTextColor("프론트엔드", 2))
                    3 -> partString = TextUtils.concat(partString, setTextColor("백엔드", 3))
                }
                partString = TextUtils.concat(partString, " · ")
            }
        }
        if (partString.isNotEmpty())
            partString = partString.dropLast(3)
        return partString
    }

    @SuppressLint("ResourceAsColor")
    private fun setTextColor(partString: String, index: Int) : SpannableStringBuilder {
        val spannableString = SpannableStringBuilder(partString)
        if (index == selectPart) {
            spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#F56457")), 0, partString.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        return spannableString
    }

    private fun initItemClickListener() {
        communityAdapter.setItemClickListener(object: ItemClickListener {
            override fun onClickItem(view: View, position: Int) {
                val id = postingList[position].id
                val intent = Intent(requireContext(), PostingActivity::class.java)
                intent.putExtra("postingId", id)
                startActivity(intent)
            }
        })
    }

    private fun goToUploadActivity() {
        binding.btnUpload.setOnClickListener {
            val intent = Intent(requireContext(), UploadActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}