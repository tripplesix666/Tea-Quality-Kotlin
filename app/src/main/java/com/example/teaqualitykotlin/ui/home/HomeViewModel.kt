package com.example.teaqualitykotlin.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.teaqualitykotlin.Tea
import com.example.teaqualitykotlin.TeasListener
import com.example.teaqualitykotlin.TeasService

class HomeViewModel(
    private val teasService: TeasService
) : ViewModel() {

    private val _tea = MutableLiveData<List<Tea>>()
    val teas: LiveData<List<Tea>> = _tea

    private val listener: TeasListener = {
        _tea.value = it
    }

    init {
        loadTeas()
        teasService.retrieveDbTeasHome()
    }

    override fun onCleared() {
        super.onCleared()
        teasService.removeListenerHome(listener)
    }

    private fun loadTeas() {
        teasService.addListenerHome(listener)
    }

    fun moveTea(tea: Tea) {
        teasService.moveTeaToFavorite(tea)
    }
}
