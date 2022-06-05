package com.example.teaqualitykotlin.ui.productPage

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.teaqualitykotlin.Tea
import com.example.teaqualitykotlin.TeaDetails
import com.example.teaqualitykotlin.TeasService
import com.example.teaqualitykotlin.TeaNotFoundException

class TeaProductPageViewModel(
    private val teasService: TeasService
) : ViewModel() {
    private val _teaDetails = MutableLiveData<TeaDetails>()
    val teaDetails: LiveData<TeaDetails> =_teaDetails

    fun loadTea(teaID: Long) {
        if (_teaDetails.value != null) return
        try {
            _teaDetails.value = teasService.getById(teaID)
        } catch (e: TeaNotFoundException) {
            e.printStackTrace()
        }
    }

    fun moveTeaToCart() {
        val teaDetails = this.teaDetails.value ?: return
        teasService.moveTeaToCart(teaDetails.tea)
    }

}