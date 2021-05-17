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

        initPartSpinner()

        initRecyclerView()

        goToUploadActivity()
    }

    private fun setPref() {
        SharedPreferenceController.apply {
            userName = getUserName(context!!).toString()
            profileImg = getProfileImg(context!!).toString()
            userPart = getUserPart(context!!)
        }
    }

    private fun initProfile() {
        binding.apply {
            tvUserName.text = userName
            Glide.with(context!!).load(profileImg).into(imgProfile)
        }
    }

    private fun initPartSpinner() {
        val item = resources.getStringArray(R.array.part_array)
        val partAdapter = ArrayAdapter(context!!, R.layout.item_part_spinner, item)
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
        communityAdapter = CommunityAdapter(context!!)
        binding.recyclerviewCommunity.apply {
            adapter = communityAdapter
            layoutManager = LinearLayoutManager(context!!)
        }
    }

    private fun getPostingData(position: Int) {
        selectPart = position
        databaseReference.child("community").addListenerForSingleValueEvent(object: ValueEventListener {
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
                val intent = Intent(context!!, PostingActivity::class.java)
                intent.putExtra("postingId", id)
                startActivity(intent)
            }
        })
    }

    private fun goToUploadActivity() {
        binding.btnUpload.setOnClickListener {
            val intent = Intent(context!!, UploadActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        updateData()
    }

    private fun updateData() {
        getPostingData(binding.spinnerPart.selectedItemPosition)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}