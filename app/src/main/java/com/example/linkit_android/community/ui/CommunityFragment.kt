package com.example.linkit_android.community.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.linkit_android.R
import com.example.linkit_android.databinding.FragmentCommunityBinding

class CommunityFragment : Fragment() {

    private var _binding: FragmentCommunityBinding? = null
    private val binding get() = _binding!!

    private var selectPart = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommunityBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initPartSpinner()
    }

    private fun initPartSpinner() {
        val item = resources.getStringArray(R.array.part_array)
        val partAdapter = ArrayAdapter(context!!, R.layout.item_part_spinner, item)
        partAdapter.setDropDownViewResource(R.layout.item_part_spinner_content)

        binding.spinnerPart.apply {
            adapter = partAdapter
            onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    selectPart = when (position) {
                        0 -> 0 // 기획
                        1 -> 1 // 디자인
                        2 -> 2 // 프론트
                        3 -> 3 // 백엔드
                        else -> -1
                    }
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}