package com.example.linkit_android.portfolio.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.linkit_android.databinding.FragmentPortfolioBinding
import com.example.linkit_android.portfolio.adapter.ProjectAdapter
import com.example.linkit_android.portfolio.adapter.ProjectData

class PortfolioFragment : Fragment() {

    private var _binding: FragmentPortfolioBinding? = null
    private val binding get() = _binding!!

    private lateinit var projectAdapter: ProjectAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPortfolioBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initProjectRecyclerView()
    }

    private fun initProjectRecyclerView() {
        projectAdapter = ProjectAdapter(context!!)

        binding.recyclerviewProject.apply {
            adapter = projectAdapter
            layoutManager = LinearLayoutManager(context!!)
        }

        projectAdapter.data = mutableListOf(
                ProjectData("https://cdn.pixabay.com/photo/2020/03/18/19/17/easter-4945288_1280.jpg", "DayBreak", "당신의 하루를 깨우는 습관 형성 앱"),
                ProjectData("https://cdn.pixabay.com/photo/2021/03/02/20/21/hare-6063733_1280.jpg", "LINK IT", "IT 프로젝트 팀 빌딩 & 네트워킹 플랫폼")
        )
        projectAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}