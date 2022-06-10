package com.example.teaqualitykotlin

import android.content.ContentValues.TAG
import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

typealias TeasListener = (teas: List<Tea>) -> Unit

class TeasService {



    var teasHome = mutableListOf<Tea>()

    private var teasFavorite = mutableListOf<Tea>()
    private var teasCart = mutableListOf<Tea>()
    private var teasSearch = mutableListOf<Tea>()

    private val listenersHome = mutableSetOf<TeasListener>()
    private val listenersFavorite = mutableSetOf<TeasListener>()
    private val listenersCart = mutableListOf<TeasListener>()
    private val listenersSearch = mutableListOf<TeasListener>()


    //Главная
    fun addListenerHome(listener: TeasListener) {
        listenersHome.add(listener)
        listener.invoke(teasHome)
    }

    fun removeListenerHome(listener: TeasListener) {
        listenersHome.remove(listener)
    }

    fun moveTeaToFavorite(tea: Tea) {
        val teaToAddToFavorite = teasHome.firstOrNull() { it.id == tea.id } ?: throw TeaNotFoundException()

        initFirebase()
        val dataMap = mutableMapOf<String, Any>()
        dataMap[CHILD_ID] = teaToAddToFavorite.id
        dataMap[CHILD_NAME] = teaToAddToFavorite.name
        dataMap[CHILD_IMAGE] = teaToAddToFavorite.image
        dataMap[CHILD_PRICE] = teaToAddToFavorite.price
        dataMap[CHILD_DETAILS] = teaToAddToFavorite.details


        REF_DATABASE_ROOT.child(NODE_USERS).child("${firebaseAuth.uid}")
            .child(NODE_FAVORITE).get().addOnSuccessListener {
                REF_DATABASE_ROOT.child(NODE_USERS).child("${firebaseAuth.uid}")
                    .child(NODE_FAVORITE).child("${it.childrenCount}").updateChildren(dataMap)
            }

        teasFavorite.add(teaToAddToFavorite)
        notifyChangesFavorite()
    }

    fun getById(id: Long): TeaDetails {
        val tea = teasHome.firstOrNull() { it.id == id} ?: throw TeaNotFoundException()
        return TeaDetails(
            tea = tea,
            details = tea.details
        )
    }

    //Поиск
    fun addListenerSearch(listener: TeasListener) {
        listenersSearch.add(listener)
        listener.invoke(teasSearch)
    }

    fun removeListenerSearch(listener: TeasListener) {
        listenersSearch.remove(listener)
    }

    private fun notifyChangesSearch() {
        listenersSearch.forEach { it.invoke(teasSearch)}
    }

    fun searchTea(newText: String) {
        teasSearch.clear()
        val searchText = newText.lowercase()

        if (searchText.isNotEmpty()) {

            teasHome.forEach {
                if (it.name.lowercase().contains(searchText)) {
                    teasSearch.add(it)
                    notifyChangesSearch()
                }
            }
        } else {
                teasSearch.clear()
                teasSearch.addAll(teasHome)
                notifyChangesSearch()
            }
    }

    //Корзина
    fun addListenerCart(listener: TeasListener) {
        listenersCart.add(listener)
        listener.invoke(teasCart)
    }

    fun removeListenerCart(listener: TeasListener) {
        listenersCart.remove(listener)
    }

    fun moveTeaToCart(tea: Tea) {

        val teaToAdd = teasHome.firstOrNull() { it.id == tea.id } ?: throw TeaNotFoundException()

        initFirebase()
        val dataMap = mutableMapOf<String, Any>()
        dataMap[CHILD_ID] = teaToAdd.id
        dataMap[CHILD_NAME] = teaToAdd.name
        dataMap[CHILD_IMAGE] = teaToAdd.image
        dataMap[CHILD_PRICE] = teaToAdd.price
        dataMap[CHILD_DETAILS] = teaToAdd.details

        REF_DATABASE_ROOT.child(NODE_USERS).child("${firebaseAuth.uid}").child(NODE_CART)
            .get().addOnSuccessListener {
            REF_DATABASE_ROOT.child(NODE_USERS).child("${firebaseAuth.uid}")
                .child(NODE_CART).child("${it.childrenCount}").updateChildren(dataMap)
        }

        teasCart.add(teaToAdd)
    }

    fun retrieveDbTeasCart() {
        initFirebase()  
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        REF_DATABASE_ROOT.child(NODE_USERS).child("${firebaseAuth.uid}")
            .child(NODE_CART).get().addOnSuccessListener { it ->
                var jsonString = it.value.toString().replace(" ","")
                Log.d(TAG, "retrieveDB: $jsonString")
                jsonString = stringToJson(jsonString)
                Log.d(TAG, "retrieveDB: $jsonString")
                val type = Types.newParameterizedType(MutableList::class.java, Tea::class.java)
                val teaListAdapter = moshi.adapter<MutableList<Tea>>(type)
                val newTeaList = teaListAdapter.fromJson(jsonString)

                if (newTeaList != null) {
                    for (t in newTeaList!!) {
                        teasCart.add(t)
                    }
                    Log.d(TAG, "retrieveDB teasFavorite: $teasCart")
                    listenersCart.forEach { it.invoke(teasCart)}
                }

            }
    }

    fun deleteTeaCart(tea: Tea) {
        val indexToDelete = teasCart.indexOfFirst { it.id == tea.id }
        if (indexToDelete != -1) {
            teasCart.removeAt(indexToDelete)
            notifyChangesCart()
        }
        initFirebase()
        REF_DATABASE_ROOT.child(NODE_USERS).child("${firebaseAuth.uid}")
            .child(NODE_CART).removeValue()

        for (tea in teasCart) {
            val dataMap = mutableMapOf<String, Any>()
            dataMap[CHILD_ID] = tea.id
            dataMap[CHILD_NAME] = tea.name
            dataMap[CHILD_IMAGE] = tea.image
            dataMap[CHILD_PRICE] = tea.price
            dataMap[CHILD_DETAILS] = tea.details

            REF_DATABASE_ROOT.child(NODE_USERS).child("${firebaseAuth.uid}").child(NODE_CART)
                .get().addOnSuccessListener {
                    REF_DATABASE_ROOT.child(NODE_USERS).child("${firebaseAuth.uid}")
                        .child(NODE_CART).child("${it.childrenCount}").updateChildren(dataMap)
                }
        }
    }

    private fun notifyChangesCart() {
        listenersCart.forEach { it.invoke(teasCart)}
    }


    //Избранное
    fun addListenerFavorite(listener: TeasListener) {
        listenersFavorite.add(listener)
//        listener.invoke(teasFavorite)
    }


    fun removeListenerFavorite(listener: TeasListener) {
        listenersFavorite.remove(listener)
    }

    fun deleteTeaFavorite(tea: Tea) {
        val indexToDelete = teasFavorite.indexOfFirst { it.id == tea.id }
        if (indexToDelete != -1) {
            teasFavorite.removeAt(indexToDelete)
            notifyChangesFavorite()
        }
        initFirebase()
        REF_DATABASE_ROOT.child(NODE_USERS).child("${firebaseAuth.uid}")
            .child(NODE_FAVORITE).removeValue()

        for (tea in teasFavorite) {
            val dataMap = mutableMapOf<String, Any>()
            dataMap[CHILD_ID] = tea.id
            dataMap[CHILD_NAME] = tea.name
            dataMap[CHILD_IMAGE] = tea.image
            dataMap[CHILD_PRICE] = tea.price
            dataMap[CHILD_DETAILS] = tea.details

            REF_DATABASE_ROOT.child(NODE_USERS).child("${firebaseAuth.uid}").child(NODE_FAVORITE)
                .get().addOnSuccessListener {
                    REF_DATABASE_ROOT.child(NODE_USERS).child("${firebaseAuth.uid}")
                        .child(NODE_FAVORITE).child("${it.childrenCount}").updateChildren(dataMap)
                }
        }
    }

    private fun notifyChangesFavorite() {
        listenersFavorite.forEach { it.invoke(teasFavorite)}
    }

    fun retrieveDbTeasFavorite() {
        initFirebase()
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        REF_DATABASE_ROOT.child(NODE_USERS).child("${firebaseAuth.uid}")
            .child(NODE_FAVORITE).get().addOnSuccessListener { it ->
            var jsonString = it.value.toString().replace(" ","")
                Log.d(TAG, "retrieveDB: $jsonString")
            jsonString = stringToJson(jsonString)
                Log.d(TAG, "retrieveDB: $jsonString")
            val type = Types.newParameterizedType(MutableList::class.java, Tea::class.java)
            val teaListAdapter = moshi.adapter<MutableList<Tea>>(type)
            val newTeaList = teaListAdapter.fromJson(jsonString)

            if (newTeaList != null) {
                for (t in newTeaList!!) {
                    teasFavorite.add(t)
                }
                Log.d(TAG, "retrieveDB teasFavorite: $teasFavorite")
                listenersFavorite.forEach { it.invoke(teasFavorite)}
            }

        }
    }


    //Заполнение листов
    fun retrieveDbTeasHome() {
        initFirebase()
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        REF_DATABASE_ROOT.child(NODE_TEAS).get().addOnSuccessListener { it ->
            var jsonString = it.value.toString().replace(" ","")
            jsonString = stringToJson(jsonString)
            val type = Types.newParameterizedType(MutableList::class.java, Tea::class.java)
            val teaListAdapter = moshi.adapter<MutableList<Tea>>(type)
            val newTeaList = teaListAdapter.fromJson(jsonString)

            for (t in newTeaList!!) {
                teasHome.add(t)
            }
            Log.d(TAG, "retrieveDB teasHome=: $teasHome")
            teasSearch.addAll(teasHome)
            listenersHome.forEach { it.invoke(teasHome)}
        }
    }

    private fun stringToJson(badString: String): String {
        var json = ""

        for (s in badString.toCharArray().indices) {
            if (badString[s] == '}' && badString[s + 1] == '{') {
                json += ""
            }
            else if (badString[s] == '{') {
                json += "{\""
            } else if (badString[s] == '=' && badString[s - 1] != 't' && badString[s - 1] != 'n') {
                json += "\":\""
            } else if (badString[s] == ',' && badString[s - 1] != '}') {
                json += "\",\""
            } else if (badString[s] == '}') {
                json += "\"}"
            }
            else {
                json += badString[s]
            }
        }
        return json
    }



}
