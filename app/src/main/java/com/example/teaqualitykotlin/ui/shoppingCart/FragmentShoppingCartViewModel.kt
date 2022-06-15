package com.example.teaqualitykotlin.ui.shoppingCart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.teaqualitykotlin.Tea
import com.example.teaqualitykotlin.TeasListener
import com.example.teaqualitykotlin.TeasService

class FragmentShoppingCartViewModel(
    private val teasService: TeasService
) : ViewModel() {

    private val _tea = MutableLiveData<List<Tea>>()
    val teas: LiveData<List<Tea>> = _tea

    var countPrice = 0
    val countOfDelivery = 149
    var totalPrice = 0
    var countOfDiscount = 0

    private val listener: TeasListener = {
        _tea.value = it
    }

    init {
        loadTeas()
        teasService.retrieveDbTeasCart()
    }

    override fun onCleared() {
        super.onCleared()
        teasService.removeListenerCart(listener)
    }

    private fun loadTeas() {
        teasService.addListenerCart(listener)
    }

    fun deleteTeaCart(tea: Tea) {
        teasService.deleteTeaCart(tea)
    }

    fun calculatePrice() {
        for (tea in teasService.teasCart) {
            countPrice += tea.price
        }
    }

    fun calculateDiscount() {
        countOfDiscount = countPrice / 100 * 1
    }
    fun calculateTotalPrice() {
        totalPrice = countPrice + countOfDelivery - countOfDiscount
    }

    fun refreshPrice() {
        countPrice = 0
        totalPrice = 0
        countOfDiscount = 0
    }

}