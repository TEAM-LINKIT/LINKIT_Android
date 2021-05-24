package com.example.linkit_android.portfolio.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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

class PortfolioFragment : Fragment() {

    private var _binding: FragmentPortfolioBinding? = null
    private val binding get() = _binding!!

    private val firebaseDatabase : FirebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference : DatabaseReference = firebaseDatabase.reference

    private lateinit var projectAdapter: ProjectAdapter
    private lateinit var toolAdapter: TagAdapter
    private lateinit var fieldAdapter: TagAdapter

    private lateinit var toolList: ArrayList<String>
    private lateinit var fieldList: ArrayList<String>

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

        getTagDataFromFirebase()

        initToolEditBtn()

        initFieldEditBtn()

        initIntroductionBtn()

        initEducationBtn()

        initProjectBtn()
    }

    /* 기존 DB에 저장되어 있던 값이 있다면 로드 */
    private fun initPortfolioContent() {
        val uid = SharedPreferenceController.getUid(requireContext()).toString()
        databaseReference.child("users").child(uid).child("portfolio").child("introduction")
                .addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (!snapshot.exists()) {
                            binding.tvNoneIntroduce.visibility = View.VISIBLE
                            binding.tvContentIntroduce.visibility = View.GONE
                        }
                        else {
                            binding.tvContentIntroduce.text = snapshot.value.toString()
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {}
                })

        databaseReference.child("users").child(uid).child("portfolio").child("education")
                .addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (!snapshot.exists()) {
                            binding.tvNoneEducation.visibility = View.VISIBLE
                            binding.tvSchool.visibility = View.GONE
                            binding.tvMajor.visibility = View.GONE
                        }
                        else {
                            binding.tvSchool.text = snapshot.child("0").value.toString()
                            binding.tvMajor.text = snapshot.child("1").value.toString()
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {}
                })
    }

    private fun initIntroductionBtn() {
        binding.btnEditIntroduce.setOnClickListener {
            val intent = Intent(requireContext(), IntroductionActivity::class.java)
            startActivityForResult(intent, 1)
        }
    }

    private fun initEducationBtn() {
        binding.btnEditEdu.setOnClickListener {
            val intent = Intent(requireContext(), EducationActivity::class.java)
            startActivityForResult(intent, 2)
        }
    }

    private fun initProjectBtn() {
        binding.btnEditProject.setOnClickListener {
            val intent = Intent(requireContext(), ProjectActivity::class.java)
            startActivityForResult(intent, 3)
        }
    }

    private fun initProjectRecyclerView() {
        val uid = SharedPreferenceController.getUid(requireContext()).toString()
        val projectList = mutableListOf<ProjectData>()

        projectAdapter = ProjectAdapter(requireContext())

        binding.recyclerviewProject.apply {
            adapter = projectAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        databaseReference.child("users").child(uid).child("portfolio").child("project")
                .addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (!snapshot.exists()) {
                            binding.tvNoneProject.visibility = View.VISIBLE
                            binding.recyclerviewProject.visibility = View.GONE
                        }
                        else {
                            for(item in snapshot.children) {
                                projectList.add(ProjectData(item.child("projectImg").value.toString(), item.child("title").value.toString(),
                                        item.child("content").value.toString(), item.child("link").value.toString()))
                            }
                            projectAdapter.data = projectList
                            projectAdapter.notifyDataSetChanged()
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {}
                })
    }

    private fun initToolRecyclerView() {
        toolList = arrayListOf()
        toolAdapter = TagAdapter(requireContext())
        FlexboxLayoutManager(requireContext()).apply {
            flexWrap = FlexWrap.WRAP
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
        }.let {
            binding.recyclerviewTool.layoutManager = it
            binding.recyclerviewTool.adapter = toolAdapter
        }
    }

    private fun initFieldRecyclerView() {
        fieldList = arrayListOf()
        fieldAdapter = TagAdapter(requireContext())
        FlexboxLayoutManager(requireContext()).apply {
            flexWrap = FlexWrap.WRAP
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
        }.let {
            binding.recyclerviewField.layoutManager = it
            binding.recyclerviewField.adapter = fieldAdapter
        }
    }

    private fun getTagDataFromFirebase() {
        val uid = SharedPreferenceController.getUid(requireContext()).toString()
        databaseReference.child("users").child(uid).child("portfolio")
                .addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        toolList.clear()
                        if (snapshot.child("tool").exists())
                            for (item in snapshot.child("tool").children)
                                toolList.add(item.value.toString())
                        else
                            toolList = arrayListOf()
                        bindToolData()

                        fieldList.clear()
                        if (snapshot.child("field").exists())
                            for (item in snapshot.child("field").children)
                                fieldList.add(item.value.toString())
                        else
                            fieldList = arrayListOf()
                        bindFieldData()
                    }
                    override fun onCancelled(error: DatabaseError) {}
                })
    }

    private fun bindToolData() {
        if (toolList.isEmpty()) {
            binding.recyclerviewTool.visibility = View.GONE
            binding.tvNoneTool.visibility = View.VISIBLE
        } else {
            binding.recyclerviewTool.visibility = View.VISIBLE
            binding.tvNoneTool.visibility = View.GONE
            toolAdapter.apply {
                data = toolList
                notifyDataSetChanged()
            }
        }
    }

    private fun bindFieldData() {
        if (fieldList.isEmpty()) {
            binding.recyclerviewField.visibility = View.GONE
            binding.tvNoneField.visibility = View.VISIBLE
        } else {
            binding.recyclerviewField.visibility = View.VISIBLE
            binding.tvNoneField.visibility = View.GONE
            fieldAdapter.apply {
                data = fieldList
                notifyDataSetChanged()
            }
        }
    }

    private fun initToolEditBtn() {
        binding.btnEditTool.setOnClickListener {
            val toolDialog = ToolDialogFragment()
            val args = Bundle()
            args.putStringArrayList("toolList", toolList)
            toolDialog.arguments = args
            toolDialog.show(childFragmentManager, "tool_dialog")
        }
    }

    private fun initFieldEditBtn() {
        binding.btnEditField.setOnClickListener {
            val fieldDialog = FieldDialogFragment()
            val args = Bundle()
            args.putStringArrayList("fieldList", fieldList)
            fieldDialog.arguments = args
            fieldDialog.show(childFragmentManager, "field_dialog")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                1 -> {
                    binding.tvContentIntroduce.text = data!!.getStringExtra("introductionContent")
                    if(binding.tvContentIntroduce.text == "") {
                        binding.tvNoneIntroduce.visibility = View.VISIBLE
                        binding.tvContentIntroduce.visibility = View.GONE
                    }
                    else {
                        binding.tvNoneIntroduce.visibility = View.GONE
                        binding.tvContentIntroduce.visibility = View.VISIBLE
                    }
                }
                2 -> {
                    val educationContentList = data!!.getStringArrayListExtra("educationContentList")
                    binding.tvSchool.text = educationContentList!![0].toString()
                    binding.tvMajor.text = educationContentList[1].toString()

                    if (binding.tvSchool.text == "" && binding.tvMajor.text == "") {
                        binding.tvNoneEducation.visibility = View.VISIBLE
                        binding.tvMajor.visibility = View.GONE
                        binding.tvSchool.visibility = View.GONE
                    }

                    else {
                        binding.tvNoneEducation.visibility = View.GONE
                        binding.tvMajor.visibility = View.VISIBLE
                        binding.tvSchool.visibility = View.VISIBLE
                    }
                }

                3 -> {
                    binding.tvNoneProject.visibility = View.GONE
                    binding.recyclerviewProject.visibility = View.VISIBLE
                    initProjectRecyclerView()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        getTagDataFromFirebase()

        initToolEditBtn()

        initFieldEditBtn()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
