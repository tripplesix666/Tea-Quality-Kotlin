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
    var teasCart = mutableListOf<Tea>()
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
                var jsonString = it.value.toString()
                jsonString = stringToJson(jsonString)
                val type = Types.newParameterizedType(MutableList::class.java, Tea::class.java)
                val teaListAdapter = moshi.adapter<MutableList<Tea>>(type)
                val newTeaList = teaListAdapter.fromJson(jsonString)

                if (newTeaList != null) {
                    for (t in newTeaList) {
                        teasCart.add(t)
                    }
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
            var jsonString = it.value.toString()
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
        val testStr = "[{ \"id\": \"0\", \"image\": \"https://firebasestorage.googleapis.com/v0/b/tea-quality-kotlin.appspot.com/o/teasImage%2F2.png?alt=media&token=f6f3a4b7-6237-49a2-8f83-b0650a235dcd\", \"name\": \"Красный чай Гуанси Хун Ча\", \"price\": \"249 Р\", \"details\": \"Красный, чай из Гуанси из местности Чжао Пин. Название переводится как черные палочки. Чай невысокого грэйда но достаточно крепкий напоминает индийский чай. Собственно этим красный чай из Гуанси отличен от классических видов китайского чая.\" }]"

        initFirebase()
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        REF_DATABASE_ROOT.child(NODE_TEAS).get().addOnSuccessListener { it ->
            var jsonString = it.value.toString()
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
        var jsonOld = ""
        var jsonNew = ""
        for (s in badString.toCharArray().indices) {
            if (badString[s] == '}' && badString[s + 1] == '{') {
                jsonOld += ""
            }
            else if (badString[s] == '{') {
                jsonOld += "{\""
            } else if (badString[s] == '=' && badString[s - 1] != 't' && badString[s - 1] != 'n') {
                jsonOld += "\":\""
            } else if (badString[s] == ',' && badString[s - 1] != '}' && (badString[s + 2] == 'i' || badString[s + 2] == 'p' || badString[s + 2] == 'n' || badString[s + 2] == 'd')) {
                jsonOld += "\",\""
            } else if (badString[s] == '}') {
                jsonOld += "\"}"
            }
            else {
                jsonOld += badString[s]
            }
        }
        for (s in jsonOld.toCharArray().indices) {
            if (jsonOld[s] == ' ' && jsonOld[s - 1] == '"' && (jsonOld[s + 1] == 'p' || jsonOld[s + 1] == 'n' || jsonOld[s + 1] == 'd' || jsonOld[s + 1] == 'i')) {

            } else {
                jsonNew += jsonOld[s]
            }
        }
        return jsonNew
    }



}
