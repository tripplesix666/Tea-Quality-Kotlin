package com.example.teaqualitykotlin

interface Navigator {

    fun showProductPage(tea: Tea)

    fun goBack()

    fun showFavorite()
}