package com.example.teaqualitykotlin

import java.io.Serializable

data class Tea (
    val id: Long,
    val productImage: Int,
    val productName: String,
    val productPrice: String,
    val details: String,
) : Serializable

data class TeaDetails(
    val tea: Tea,
    val details: String,
)
