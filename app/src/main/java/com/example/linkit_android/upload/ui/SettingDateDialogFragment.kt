package com.example.linkit_android.upload.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.fragment.app.DialogFragment
import com.example.linkit_android.databinding.DialogSettingDateBinding
import com.example.linkit_android.util.setDialogSize
import java.util.*

class SettingDateDialogFragment : DialogFragment() {

    private var _binding: DialogSettingDateBinding? = null
    private val binding get() = _binding!!

    private var selectYear = 0
    private var selectMonth = 0

    private lateinit var year: NumberPicker
    private lateinit var month: NumberPicker

    private lateinit var currentDate: Calendar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _binding = DialogSettingDateBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDatePicker()
    }

    private fun initDatePicker() {
        year = binding.includeYmPicker.year
        month = binding.includeYmPicker.month

        setDateRange()

        initDateValue()

        setDateValue()

        setPickerLimit()

        setListenerOnDatePicker()
    }

    private fun setDateValue() {
        selectYear = year.value
        selectMonth = month.value
    }

    private fun setDateRange() {
        // 현재 날짜 가져오기
        currentDate = Calendar.getInstance()

        // minValue = 최소 날짜 표시
        year.minValue = currentDate.get(Calendar.YEAR) - 1
        month.minValue = 1

        // maxValue = 최대 날짜 표시
        year.maxValue = currentDate.get(Calendar.YEAR) + 1
        month.maxValue = 12
    }

    private fun initDateValue() {
        year.value = currentDate.get(Calendar.YEAR)
        month.value = currentDate.get(Calendar.MONTH) + 1
    }

    private fun setPickerLimit() {
        // 순환 안되게 막기
        year.wrapSelectorWheel = false
        month.wrapSelectorWheel = false

        //editText 입력 방지
        year.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        month.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
    }

    private fun setListenerOnDatePicker() {
        // year picker change listener
        year.setOnValueChangedListener { _, _, _ ->
            setDateValue()
        }

        // month picker change listener
        month.setOnValueChangedListener { _, _, _ ->
            setDateValue()
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