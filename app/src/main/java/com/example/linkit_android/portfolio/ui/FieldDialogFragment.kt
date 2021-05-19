package com.example.linkit_android.portfolio.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.linkit_android.databinding.DialogFieldBinding
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

class FieldDialogFragment : DialogFragment() {

    private var _binding: DialogFieldBinding? = null
    private val binding get() = _binding!!

    private lateinit var inputFieldAdapter: FillRedTagAdapter
    private lateinit var exampleFieldAdapter: StrokeRedTagAdapter

    private lateinit var inputFieldList: MutableList<String>

    private lateinit var uid: String

    private val firebaseDatabase : FirebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference : DatabaseReference = firebaseDatabase.reference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _binding = DialogFieldBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setPref()

        getIntentValue()

        setRecyclerViewVisibility(inputFieldList)

        initInputFieldRecyclerView()

        initExampleFieldRecyclerView()

        initAddBtn()

        initOkBtn()

        initCancelBtn()
    }

    private fun setPref() {
        uid = SharedPreferenceController.getUid(context!!).toString()
    }

    private fun getIntentValue() {
        inputFieldList = mutableListOf()
    }

    private fun setRecyclerViewVisibility(data: MutableList<String>) {
        if (data.isEmpty())
            binding.constraintlayoutInputField.visibility = View.GONE
        else
            binding.constraintlayoutInputField.visibility = View.VISIBLE
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
            data = inputFieldList
            initInputItemClickListener()
            notifyDataSetChanged()
        }
    }

    private fun initInputItemClickListener() {
        inputFieldAdapter.setItemClickListener(object: ItemClickListener {
            override fun onClickItem(view: View, position: Int) {
                setRecyclerViewVisibility(inputFieldList)
            }
        })
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
            initExampleItemClickListener()
            notifyDataSetChanged()
        }
    }

    private fun initExampleItemClickListener() {
        exampleFieldAdapter.setItemClickListener(object: ItemClickListener {
            override fun onClickItem(view: View, position: Int) {
                addExampleItemToRecyclerView(position)
            }
        })
    }

    private fun addExampleItemToRecyclerView(position: Int) {
        if (inputFieldAdapter.data.isEmpty())
            addExampleItemToAdapter(position)
        else
            if (!checkDuplicate(exampleFieldAdapter.data[position]))
                addExampleItemToAdapter(position)
    }

    private fun checkDuplicate(checkData: String) : Boolean {
        var flag = false
        for (item in inputFieldAdapter.data) {
            if (item == checkData) {
                flag = true
                break
            }
        }
        return flag
    }

    private fun addExampleItemToAdapter(position: Int) {
        inputFieldAdapter.apply {
            data.add(exampleFieldAdapter.data[position])
            setRecyclerViewVisibility(data)
            notifyDataSetChanged()
        }
    }

    private fun initAddBtn() {
        binding.btnAddTag.setOnClickListener {
            val inputData = binding.etContent.text.toString()
            if (inputData != "") {
                if (inputFieldAdapter.data.isEmpty())
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
        inputFieldAdapter.apply {
            data.add(inputData)
            setRecyclerViewVisibility(data)
            notifyDataSetChanged()
        }
    }

    private fun initOkBtn() {
        binding.buttonOk.setOnClickListener {
            databaseReference.child("users").child(uid).child("portfolio")
                    .child("field").setValue(inputFieldAdapter.data)
            hideKeyboard(context!!, binding.etContent)
            this.dismiss()
        }
    }

    private fun initCancelBtn() {
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