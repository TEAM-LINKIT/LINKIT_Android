package com.example.linkit_android.portfolio.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.linkit_android.R

class ProjectAdapter(private val context: Context) : RecyclerView.Adapter<ProjectViewHolder>() {

    var data = mutableListOf<ProjectData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_project, parent, false)
        return ProjectViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        holder.onBind(data[position])

        setLinkBtnVisibility(holder, position)
        openProjectUri(holder, position)
    }

    override fun getItemCount(): Int = data.size

    private fun setLinkBtnVisibility(holder: ProjectViewHolder, position: Int) {
        // link 값이 false 일 때 link 버튼 invisible 처리
        if (data[position].link.isEmpty()) {
            holder.link.visibility = View.INVISIBLE
        }
    }

    private fun openProjectUri(holder: ProjectViewHolder, position: Int) {
        if (data[position].link.isNotEmpty()) {
            holder.link.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(data[position].link))
                context.startActivity(intent)
            }
        }
    }
}