package com.example.linkit_android.community.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.linkit_android.R
import com.example.linkit_android.community.adapter.CommunityAdapter
import com.example.linkit_android.community.adapter.CommunityData
import com.example.linkit_android.databinding.FragmentCommunityBinding

class CommunityFragment : Fragment() {

    private var _binding: FragmentCommunityBinding? = null
    private val binding get() = _binding!!

    private lateinit var communityAdapter: CommunityAdapter

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

        initRecyclerView()
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

    private fun initRecyclerView() {
        communityAdapter = CommunityAdapter(context!!)

        binding.recyclerviewCommunity.apply {
            adapter = communityAdapter
            layoutManager = LinearLayoutManager(context!!)
        }

        communityAdapter.data = mutableListOf(
            CommunityData("함께 웹 개발 하실 분 구해요!", "기획 · 디자인 · 프론트엔드 · 백엔드"),
            CommunityData("함께 토이프로젝트 진행할 고등학생 있나요?", "프론트엔드"),
            CommunityData("한이음 공모전 같이 나갈 사람 있나요?", "프론트엔드 · 백엔드"),
            CommunityData("안녕하세요! 웹 개발 공모전 함께 나갈 고등학생 구합니다. 저도 고등학생입니다!", "기획 · 디자인 · 프론트엔드 · 백엔드"),
            CommunityData("한이음 공모전 같이 나갈 사람 있나요?", "프론트엔드 · 백엔드"),
            CommunityData("함께 웹 개발 하실 분 구해요!", "기획 · 디자인 · 프론트엔드 · 백엔드")
        )
        communityAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}