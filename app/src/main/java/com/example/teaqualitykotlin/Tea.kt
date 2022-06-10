package com.example.teaqualitykotlin

import java.io.Serializable

data class Tea (
    val id: Long,
    val image: String,
    val name: String,
    val price: String,
    val details: String,
) : Serializable

data class TeaDetails(
    val tea: Tea,
    val details: String,
)
