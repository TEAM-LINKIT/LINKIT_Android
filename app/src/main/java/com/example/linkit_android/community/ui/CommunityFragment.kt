package com.example.linkit_android.community.ui

import android.content.Intent
import android.os.Bundle
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
import com.example.linkit_android.upload.ui.UploadActivity
import com.example.linkit_android.util.SharedPreferenceController
import com.google.firebase.database.*
import java.lang.StringBuilder

class CommunityFragment : Fragment() {

    private var _binding: FragmentCommunityBinding? = null
    private val binding get() = _binding!!

    private lateinit var communityAdapter: CommunityAdapter
    private var postingList: MutableList<PostingModel> = mutableListOf()

    private var selectPart = -1

    private lateinit var userName: String
    private lateinit var profileImg: String

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

        initRecyclerView()

        initPartSpinner()

        goToUploadActivity()
    }

    private fun setPref() {
        SharedPreferenceController.apply {
            userName = getUserName(context!!).toString()
            profileImg = getProfileImg(context!!).toString()
        }
    }

    private fun initProfile() {
        binding.apply {
            tvUserName.text = userName
            Glide.with(context!!).load(profileImg).into(imgProfile)
        }
    }

    private fun initRecyclerView() {
        communityAdapter = CommunityAdapter(context!!)
        binding.recyclerviewCommunity.apply {
            adapter = communityAdapter
            layoutManager = LinearLayoutManager(context!!)
        }
    }

    private fun initPartSpinner() {
        val item = resources.getStringArray(R.array.part_array)
        val partAdapter = ArrayAdapter(context!!, R.layout.item_part_spinner, item)
        partAdapter.setDropDownViewResource(R.layout.item_part_spinner_content)

        binding.spinnerPart.apply {
            adapter = partAdapter
            onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
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
    }

    private fun getPostingData(position: Int) {
        selectPart = position
        databaseReference.child("community").addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                postingList.clear()
                for (data in snapshot.children) {
                    val postingData = data.getValue(PostingModel::class.java)
                    if (postingData?.recruitNum!![position] != 0)
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
    }

    private fun setPartListData(partList: MutableList<Int>) : String {
        val partString = StringBuilder()
        if (partList[0] != 0)
            partString.append("기획")
        if (partList[1] != 0)
            partString.append("디자인")
        if (partList[2] != 0)
            partString.append("프론드엔드")
        if (partList[3] != 0)
            partString.append("백엔드")
        return partString.toString()
    }

    private fun goToUploadActivity() {
        binding.btnUpload.setOnClickListener {
            val intent = Intent(context!!, UploadActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}