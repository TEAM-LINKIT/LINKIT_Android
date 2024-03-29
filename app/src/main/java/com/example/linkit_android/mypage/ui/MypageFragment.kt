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
import com.example.linkit_android.portfolio.ui.ToolDialogFragment
import com.example.linkit_android.upload.ui.SendPortfolioDialogFragment
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

        initWithdrawalBtn()

        initLogoutBtn()
    }

    private fun initMypage() {
        val name = SharedPreferenceController.getUserName(requireContext()).toString()
        val part = SharedPreferenceController.getUserPart(requireContext())
        val profileImg = SharedPreferenceController.getProfileImg(requireContext()).toString()

        binding.tvUserName.text = name
        binding.tvPart.text = getPartString(part)

        Glide.with(requireContext()).load(profileImg).into(binding.imgProfile)
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

    private fun initLogoutBtn() {
        binding.btnLogout.setOnClickListener {
            val logoutDialog = LogoutDialogFragment()
            logoutDialog.show(childFragmentManager, "withdrawal_dialog")
        }
    }

    private fun initWithdrawalBtn() {
        binding.btnWithdraw.setOnClickListener {
            val withdrawalDialog = WithdrawalDialogFragment()
            withdrawalDialog.show(childFragmentManager, "withdrawal_dialog")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}