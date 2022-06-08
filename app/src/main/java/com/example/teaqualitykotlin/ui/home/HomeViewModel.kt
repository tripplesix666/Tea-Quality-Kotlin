package com.example.teaqualitykotlin.ui.home

import android.content.ContentValues.TAG
import android.util.Log
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
        Log.d(TAG, "onCreate: view model")

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