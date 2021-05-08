package com.example.linkit_android.upload.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import com.example.linkit_android.databinding.DialogSelectPartBinding
import com.example.linkit_android.upload.ui.UploadActivity.Companion.upload_backend_count
import com.example.linkit_android.upload.ui.UploadActivity.Companion.upload_design_count
import com.example.linkit_android.upload.ui.UploadActivity.Companion.upload_frontend_count
import com.example.linkit_android.upload.ui.UploadActivity.Companion.upload_plan_count
import com.example.linkit_android.util.setDialogSize

class SelectPartDialogFragment(val itemClick: (IntArray) -> Unit) : DialogFragment() {

    private var _binding: DialogSelectPartBinding? = null
    private val binding get() = _binding!!

    private var planCount = 0
    private var designCount = 0
    private var frontendCount = 0
    private var backendCount = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _binding = DialogSelectPartBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCountValue()

        initPlusBtn()

        initMinusBtn()

        initOkBtn()

        initCancelBtn()
    }

    private fun initCountValue() {
        planCount = upload_plan_count
        designCount = upload_design_count
        frontendCount = upload_frontend_count
        backendCount = upload_backend_count
        setCountValue()
    }

    private fun setCountValue() {
        binding.apply {
            tvPlanCount.text = planCount.toString()
            tvDesignCount.text = designCount.toString()
            tvFrontCount.text = frontendCount.toString()
            tvBackCount.text = backendCount.toString()
        }
    }

    private fun initPlusBtn() {
        binding.apply {
            btnPlusPlan.setClickListenerOnPlusBtn()
            btnPlusDesign.setClickListenerOnPlusBtn()
            btnPlusFront.setClickListenerOnPlusBtn()
            btnPlusBack.setClickListenerOnPlusBtn()
        }
    }

    private fun ConstraintLayout.setClickListenerOnPlusBtn() {
        this.setOnClickListener {
            binding.apply {
                when (this@setClickListenerOnPlusBtn.id) {
                    btnPlusPlan.id -> tvPlanCount.text = (++planCount).toString()
                    btnPlusDesign.id -> tvDesignCount.text = (++designCount).toString()
                    btnPlusFront.id -> tvFrontCount.text = (++frontendCount).toString()
                    btnPlusBack.id -> tvBackCount.text = (++backendCount).toString()
                }
            }
        }
    }

    private fun initMinusBtn() {
        binding.apply {
            btnMinusPlan.setClickListenerOnMinusBtn()
            btnMinusDesign.setClickListenerOnMinusBtn()
            btnMinusFront.setClickListenerOnMinusBtn()
            btnMinusBack.setClickListenerOnMinusBtn()
        }
    }

    private fun ConstraintLayout.setClickListenerOnMinusBtn() {
        this.setOnClickListener {
            binding.apply {
                when (this@setClickListenerOnMinusBtn.id) {
                    btnMinusPlan.id -> planCount = checkNegativeNum(planCount)
                    btnMinusDesign.id -> designCount = checkNegativeNum(designCount)
                    btnMinusFront.id -> frontendCount = checkNegativeNum(frontendCount)
                    btnMinusBack.id -> backendCount = checkNegativeNum(backendCount)
                }
                setCountValue()
            }
        }
    }

    private fun checkNegativeNum(count: Int) : Int {
        return if (count <= 0) 0
        else count - 1
    }

    /* 확인 버튼 클릭 */

    private fun initOkBtn() {
        binding.buttonOk.setOnClickListener {
            val pick = intArrayOf(planCount, designCount, frontendCount, backendCount)
            itemClick(pick)
            this.dismiss()
        }
    }

    /* 취소 버튼 클릭 */

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