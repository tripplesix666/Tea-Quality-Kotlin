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

}