package com.example.linkit_android.portfolio.ui

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

        getIntentValue()

        setRecyclerViewVisibility(inputToolList)

        initInputToolRecyclerView()

        initExampleToolRecyclerView()

        initAddBtn()

        initOkBtn()

        initCancel()
    }

    /*
        PortfolioFragment
        1. firebase에서 data 불러오기 -> 없으면 내용이 없습니다 출력 (onResume)
        2. 불러온 data intent에 담아 ToolDialogFrag로 보내기
        3. ToolDialogFrag에서 intent 받기
    */

    private fun setPref() {
        uid = SharedPreferenceController.getUid(context!!).toString()
    }

    private fun getIntentValue() {
        // Todo: intent로 받아온 배열 값이 empty가 아니면 inputToolList에 넣어주고, empty일 경우 inputToolList를 mutableListOf()로 초기화
        inputToolList = mutableListOf()
    }

    private fun setRecyclerViewVisibility(data: MutableList<String>) {
        if (data.isEmpty())
            binding.constraintlayoutInputTool.visibility = View.GONE
        else
            binding.constraintlayoutInputTool.visibility = View.VISIBLE
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
                Toast.makeText(context!!, "내용을 입력해주세요", Toast.LENGTH_SHORT).show()
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
            hideKeyboard(context!!, binding.etContent)
            this.dismiss()
        }
    }

    private fun initCancel() {
        binding.buttonCancel.setOnClickListener {
            hideKeyboard(context!!, binding.etContent)
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