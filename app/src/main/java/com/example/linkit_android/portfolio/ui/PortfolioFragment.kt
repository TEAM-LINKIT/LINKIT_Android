package com.example.linkit_android.portfolio.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.linkit_android.databinding.FragmentPortfolioBinding
import com.example.linkit_android.portfolio.adapter.ProjectAdapter
import com.example.linkit_android.portfolio.adapter.ProjectData
import com.example.linkit_android.portfolio.adapter.TagAdapter
import com.example.linkit_android.util.SharedPreferenceController
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.firebase.database.*
import com.google.firebase.database.snapshot.EmptyNode

class PortfolioFragment : Fragment() {

    private var _binding: FragmentPortfolioBinding? = null
    private val binding get() = _binding!!

    private val firebaseDatabase : FirebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference : DatabaseReference = firebaseDatabase.reference

    private lateinit var projectAdapter: ProjectAdapter
    private lateinit var toolAdapter: TagAdapter
    private lateinit var fieldAdapter: TagAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPortfolioBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initPortfolioContent()

        initProjectRecyclerView()

        initToolRecyclerView()

        initFieldRecyclerView()

        initToolEditBtn()

        initFieldEditBtn()

        initIntroductionBtn()
    }

    /* 기존 DB에 저장되어 있던 값이 있다면 로드 */
    private fun initPortfolioContent() {
        val uid = SharedPreferenceController.getUid(context!!).toString()
        databaseReference.child("users").child(uid).child("portfolio")
                .addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (!snapshot.child("introduction").exists()) {
                            binding.tvNoneIntroduce.visibility = View.VISIBLE
                            binding.tvContentIntroduce.visibility = View.GONE
                        }
                        else {
                            binding.tvContentIntroduce.text = snapshot.child("introduction").value.toString()
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {}
                })
    }


    private fun initIntroductionBtn() {
        binding.btnEditIntroduce.setOnClickListener {
            val intent = Intent(context!!, IntroductionActivity::class.java)
            startActivityForResult(intent, 1)
        }
    }


    private fun initProjectRecyclerView() {
        projectAdapter = ProjectAdapter(context!!)

        binding.recyclerviewProject.apply {
            adapter = projectAdapter
            layoutManager = LinearLayoutManager(context!!)
        }

        projectAdapter.data = mutableListOf(
                ProjectData("https://cdn.pixabay.com/photo/2020/03/18/19/17/easter-4945288_1280.jpg", "DayBreak", "당신의 하루를 깨우는 습관 형성 앱", false),
                ProjectData("https://cdn.pixabay.com/photo/2021/03/02/20/21/hare-6063733_1280.jpg", "LINK IT", "IT 프로젝트 팀 빌딩 & 네트워킹 플랫폼", false)
        )
        projectAdapter.notifyDataSetChanged()
    }

    private fun initToolRecyclerView() {
        toolAdapter = TagAdapter(context!!)

        FlexboxLayoutManager(context!!).apply {
            flexWrap = FlexWrap.WRAP
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
        }.let {
            binding.recyclerviewTool.layoutManager = it
            binding.recyclerviewTool.adapter = toolAdapter
        }

        toolAdapter.apply {
            data = mutableListOf("여기는 협업툴 자리", "Github", "Slack", "Notion", "Trello", "Figma")
            notifyDataSetChanged()
        }
    }

    private fun initFieldRecyclerView() {
        fieldAdapter = TagAdapter(context!!)

        FlexboxLayoutManager(context!!).apply {
            flexWrap = FlexWrap.WRAP
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
        }.let {
            binding.recyclerviewField.layoutManager = it
            binding.recyclerviewField.adapter = fieldAdapter
        }

        fieldAdapter.apply {
            data = mutableListOf("여기는 활동 분야 자리", "Android", "Kotlin", "GitHub", "Java")
            notifyDataSetChanged()
        }
    }

    private fun initToolEditBtn() {
        binding.btnEditTool.setOnClickListener {
            val toolDialog = ToolDialogFragment()
            toolDialog.show(childFragmentManager, "tool_dialog")
        }
    }

    private fun initFieldEditBtn() {
        binding.btnEditField.setOnClickListener {
            val fieldDialog = FieldDialogFragment()
            fieldDialog.show(childFragmentManager, "field_dialog")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                1 -> {
                    /* getStringExtra()는 인텐트 값 가지고 오는 */
                    binding.tvContentIntroduce.text = data!!.getStringExtra("introductionContent")
                    /* 기존 내용 다 지우고 저장 누른 경우 */
                    if(binding.tvContentIntroduce.text == "") {
                        binding.tvNoneIntroduce.visibility = View.VISIBLE
                        binding.tvContentIntroduce.visibility = View.GONE
                    }
                    else {
                        binding.tvNoneIntroduce.visibility = View.GONE
                        binding.tvContentIntroduce.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
