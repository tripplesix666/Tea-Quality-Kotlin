package com.example.teaqualitykotlin.ui.favorite

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.teaqualitykotlin.*
import com.example.teaqualitykotlin.adapters.AdapterHomeTeas

class FragmentFavoriteViewModel(
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
        teasService.removeListenerFavorite(listener)
    }

    private fun loadTeas() {
        teasService.addListenerFavorite(listener)
    }

    fun deleteTea(tea: Tea) {
        teasService.deleteTeaFavorite(tea)
    }

    val count: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun count(adapter: AdapterHomeTeas): String {

        count.value = adapter.itemCount.toString()
        return count.value.toString()

    }

    fun populateDb() {
        initFirebase()
        teasService.retrieveDB()
        for (tea in teasService.teasHome) {

            val id = tea.id.toString()

            val dateMap = mutableMapOf<String, Any>()
            dateMap[CHILD_ID] = tea.id
            dateMap[CHILD_NAME] = tea.productName
            dateMap[CHILD_PRICE] = tea.productPrice
            dateMap[CHILD_DETAILS] = tea.details

            REF_DATABASE_ROOT.child(NODE_TEAS).child(id).updateChildren(dateMap)

            REF_STORAGE_ROOT.child("teasImage/1/1.png").downloadUrl.addOnSuccessListener {
                REF_DATABASE_ROOT.child(NODE_TEAS).child(id).child(CHILD_IMAGE).setValue(it.toString())
            }
        }
    }
}
