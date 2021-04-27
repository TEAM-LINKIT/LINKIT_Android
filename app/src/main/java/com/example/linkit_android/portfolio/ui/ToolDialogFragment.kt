package com.example.linkit_android.portfolio.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.linkit_android.databinding.DialogToolBinding
import com.example.linkit_android.portfolio.adapter.FillRedTagAdapter
import com.example.linkit_android.portfolio.adapter.StrokeRedTagAdapter
import com.example.linkit_android.util.setDialogSize
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class ToolDialogFragment : DialogFragment() {

    private var _binding: DialogToolBinding? = null
    private val binding get() = _binding!!

    private lateinit var inputToolAdapter: FillRedTagAdapter
    private lateinit var exampleToolAdapter: StrokeRedTagAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _binding = DialogToolBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initInputToolRecyclerView()

        initExampleToolRecyclerView()
    }

    private fun initInputToolRecyclerView() {
        inputToolAdapter = FillRedTagAdapter(context!!)

        FlexboxLayoutManager(context!!).apply {
            flexWrap = FlexWrap.WRAP
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
        }.let {
            binding.recyclerviewInputTool.layoutManager = it
            binding.recyclerviewInputTool.adapter = inputToolAdapter
        }

        inputToolAdapter.apply {
            data = mutableListOf("GitHub", "Slack", "Figma")
            notifyDataSetChanged()
        }
    }

    private fun initExampleToolRecyclerView() {
        exampleToolAdapter = StrokeRedTagAdapter(context!!)

        FlexboxLayoutManager(context!!).apply {
            flexWrap = FlexWrap.WRAP
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
        }.let {
            binding.recyclerviewExampleTool.layoutManager = it
            binding.recyclerviewExampleTool.adapter = exampleToolAdapter
        }

        exampleToolAdapter.apply {
            data = mutableListOf("GitHub", "Notion", "Trello", "Slack", "Jira", "Zeplin", "Figma")
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