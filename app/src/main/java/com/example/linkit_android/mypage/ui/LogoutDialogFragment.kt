package com.example.linkit_android.mypage.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.linkit_android.databinding.DialogBasicBinding
import com.example.linkit_android.login.LoginActivity
import com.example.linkit_android.util.SharedPreferenceController
import com.example.linkit_android.util.setDialogSize

class LogoutDialogFragment : DialogFragment() {

    private var _binding: DialogBasicBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _binding = DialogBasicBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setContentTextView()

        initOkBtn()

        initCancelBtn()
    }

    private fun setContentTextView() {
        binding.tvContent.text = "로그아웃 하시겠습니까?"
    }

    private fun initOkBtn() {
        binding.buttonOk.setOnClickListener {
            SharedPreferenceController.clearAll(requireContext())
            goToLoginActivity()
            this.dismiss()
        }
    }

    private fun goToLoginActivity() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun initCancelBtn() {
        binding.buttonCancel.setOnClickListener {
            this.dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        setDialogSize(dialog, activity)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}