package com.example.linkit_android.model

data class PortfolioModel (
        val introduction: String?,
        val education: MutableList<String>?,
        val project: MutableList<String>?,
        val tool: MutableList<String>?,
        val field: MutableList<String>?
)
