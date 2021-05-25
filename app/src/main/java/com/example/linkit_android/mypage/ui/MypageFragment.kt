package com.example.linkit_android.mypage.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.linkit_android.chatting.ui.ChatRoomActivity
import com.example.linkit_android.databinding.FragmentMypageBinding
import com.example.linkit_android.util.SharedPreferenceController
import com.example.linkit_android.util.getPartString

class MypageFragment : Fragment() {

    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMypageBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMypage()

        initWriteListBtn()

        initRecommendListBtn()
    }

    private fun initMypage() {
        val name = SharedPreferenceController.getUserName(context!!).toString()
        val part = SharedPreferenceController.getUserPart(context!!)
        val profileImg = SharedPreferenceController.getProfileImg(context!!).toString()

        binding.tvUserName.text = name
        binding.tvPart.text = getPartString(part)

        Glide.with(context!!).load(profileImg).into(binding.imgProfile)
    }

    private fun initWriteListBtn() {
        binding.btnWriteList.setOnClickListener {
            val intent = Intent(context, PostingListActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initRecommendListBtn() {
        binding.btnRecommendList.setOnClickListener {
            val intent = Intent(context, RecommendListActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}