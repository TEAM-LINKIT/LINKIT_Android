package com.example.linkit_android.portfolio.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.linkit_android.databinding.DialogFieldBinding
import com.example.linkit_android.portfolio.adapter.FillRedTagAdapter
import com.example.linkit_android.portfolio.adapter.StrokeRedTagAdapter
import com.example.linkit_android.util.setDialogSize
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class FieldDialogFragment : DialogFragment() {

    private var _binding: DialogFieldBinding? = null
    private val binding get() = _binding!!

    private lateinit var inputFieldAdapter: FillRedTagAdapter
    private lateinit var exampleFieldAdapter: StrokeRedTagAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _binding = DialogFieldBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initInputFieldRecyclerView()

        initExampleFieldRecyclerView()
    }

    private fun initInputFieldRecyclerView() {
        inputFieldAdapter = FillRedTagAdapter(context!!)

        FlexboxLayoutManager(context!!).apply {
            flexWrap = FlexWrap.WRAP
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
        }.let {
            binding.recyclerviewInputField.layoutManager = it
            binding.recyclerviewInputField.adapter = inputFieldAdapter
        }

        inputFieldAdapter.apply {
            data = mutableListOf("aws", "Java script")
            notifyDataSetChanged()
        }
    }

    private fun initExampleFieldRecyclerView() {
        exampleFieldAdapter = StrokeRedTagAdapter(context!!)

        FlexboxLayoutManager(context!!).apply {
            flexWrap = FlexWrap.WRAP
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
        }.let {
            binding.recyclerviewExampleField.layoutManager = it
            binding.recyclerviewExampleField.adapter = exampleFieldAdapter
        }

        exampleFieldAdapter.apply {
            data = mutableListOf("서비스기획", "컨텐츠기획", "Web Design", "App Design",
                    "Android", "iOS", "HTML", "MySQL", "Spring", "Node.js")
            notifyDataSetChanged()
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