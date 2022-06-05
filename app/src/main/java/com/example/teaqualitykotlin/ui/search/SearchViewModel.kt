package com.example.teaqualitykotlin.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.teaqualitykotlin.Tea
import com.example.teaqualitykotlin.TeasListener
import com.example.teaqualitykotlin.TeasService

class SearchViewModel(
    private val teasService: TeasService
) : ViewModel() {

    private val _tea = MutableLiveData<List<Tea>>()
    val teas: LiveData<List<Tea>> = _tea

    private val listener: TeasListener = {
        _tea.value = it
    }

    init {
        loadTeas()
    }

    override fun onCleared() {
        super.onCleared()
        teasService.removeListenerSearch(listener)
    }

    private fun loadTeas() {
        teasService.addListenerSearch(listener)
    }

    fun moveTea(tea: Tea) {
        teasService.moveTeaToFavorite(tea)
    }
    fun searchTea(newText: String) {
        teasService.searchTea(newText)
    }
}