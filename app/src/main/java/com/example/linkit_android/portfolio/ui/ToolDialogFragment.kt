package com.example.linkit_android.portfolio.ui

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.linkit_android.databinding.DialogToolBinding
import com.example.linkit_android.portfolio.adapter.FillRedTagAdapter
import com.example.linkit_android.portfolio.adapter.StrokeRedTagAdapter
import com.example.linkit_android.util.ItemClickListener
import com.example.linkit_android.util.SharedPreferenceController
import com.example.linkit_android.util.hideKeyboard
import com.example.linkit_android.util.setDialogSize
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ToolDialogFragment : DialogFragment() {

    private var _binding: DialogToolBinding? = null
    private val binding get() = _binding!!

    private lateinit var inputToolAdapter: FillRedTagAdapter
    private lateinit var exampleToolAdapter: StrokeRedTagAdapter

    private lateinit var inputToolList: MutableList<String>

    private lateinit var uid: String

    private val firebaseDatabase : FirebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference : DatabaseReference = firebaseDatabase.reference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _binding = DialogToolBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setPref()

        getBundle()

        setRecyclerViewVisibility(inputToolList)

        initInputToolRecyclerView()

        initExampleToolRecyclerView()

        initAddBtn()

        initOkBtn()

        initCancel()
    }

    private fun setPref() {
        uid = SharedPreferenceController.getUid(requireContext()).toString()
    }

    private fun getBundle() {
        val toolList = requireArguments().getStringArrayList("toolList")!!
        inputToolList = if (toolList.isEmpty())
            mutableListOf()
        else
            toolList
    }

    private fun setRecyclerViewVisibility(data: MutableList<String>) {
        if (data.isEmpty())
            binding.constraintlayoutInputTool.visibility = View.GONE
        else
            binding.constraintlayoutInputTool.visibility = View.VISIBLE
    }

    private fun initInputToolRecyclerView() {
        inputToolAdapter = FillRedTagAdapter(requireContext())

        FlexboxLayoutManager(requireContext()).apply {
            flexWrap = FlexWrap.WRAP
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
        }.let {
            binding.recyclerviewInputTool.layoutManager = it
            binding.recyclerviewInputTool.adapter = inputToolAdapter
        }

        inputToolAdapter.apply {
            data = inputToolList
            initInputItemClickListener()
            notifyDataSetChanged()
        }
    }

    private fun initInputItemClickListener() {
        inputToolAdapter.setItemClickListener(object: ItemClickListener {
            override fun onClickItem(view: View, position: Int) {
                setRecyclerViewVisibility(inputToolList)
            }
        })
    }

    private fun initExampleToolRecyclerView() {
        exampleToolAdapter = StrokeRedTagAdapter(requireContext())

        FlexboxLayoutManager(requireContext()).apply {
            flexWrap = FlexWrap.WRAP
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
        }.let {
            binding.recyclerviewExampleTool.layoutManager = it
            binding.recyclerviewExampleTool.adapter = exampleToolAdapter
        }

        exampleToolAdapter.apply {
            data = mutableListOf("GitHub", "Notion", "Trello", "Slack", "Jira", "Zeplin", "Figma")
            initExampleItemClickListener()
            notifyDataSetChanged()
        }
    }

    private fun initExampleItemClickListener() {
        exampleToolAdapter.setItemClickListener(object: ItemClickListener {
            override fun onClickItem(view: View, position: Int) {
                addExampleItemToRecyclerView(position)
            }
        })
    }

    private fun addExampleItemToRecyclerView(position: Int) {
        if (inputToolAdapter.data.isEmpty())
            addExampleItemToAdapter(position)
        else {
            if (!checkDuplicate(exampleToolAdapter.data[position]))
                addExampleItemToAdapter(position)
        }
    }

    private fun checkDuplicate(checkData: String) : Boolean {
        var flag = false
        for (item in inputToolAdapter.data) {
            if (item == checkData) {
                flag = true
                break
            }
        }
        return flag
    }

    private fun addExampleItemToAdapter(position: Int) {
        inputToolAdapter.apply {
            data.add(exampleToolAdapter.data[position])
            setRecyclerViewVisibility(data)
            notifyDataSetChanged()
        }
    }

    private fun initAddBtn() {
        binding.btnAddTag.setOnClickListener {
            val inputData = binding.etContent.text.toString()
            if (inputData != "") {
                if (inputToolAdapter.data.isEmpty())
                    addInputDataToRecyclerView(inputData)
                else {
                    if (!checkDuplicate(inputData))
                        addInputDataToRecyclerView(inputData)
                }
            } else {
                Toast.makeText(requireContext(), "내용을 입력해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addInputDataToRecyclerView(inputData: String) {
        binding.etContent.setText("")
        inputToolAdapter.apply {
            data.add(inputData)
            setRecyclerViewVisibility(data)
            notifyDataSetChanged()
        }
    }

    private fun initOkBtn() {
        binding.buttonOk.setOnClickListener {
            databaseReference.child("users").child(uid).child("portfolio")
                    .child("tool").setValue(inputToolAdapter.data)
            hideKeyboard(requireContext(), binding.etContent)
            this.dismiss()
        }
    }

    private fun initCancel() {
        binding.buttonCancel.setOnClickListener {
            hideKeyboard(requireContext(), binding.etContent)
            this.dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        val portfolioFragment = parentFragment
        portfolioFragment!!.onResume()
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