package com.example.teaqualitykotlin

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

typealias TeasListener = (teas: List<Tea>) -> Unit

class TeasService {

    private var teaTest = mutableListOf<TeaTest>()

    var teasHome = mutableListOf<Tea>()
    private var teasFavorite = mutableListOf<Tea>()
    private var teasCart = mutableListOf<Tea>()
    private var teasSearch = mutableListOf<Tea>()

    private val listenersHome = mutableSetOf<TeasListener>()
    private val listenersFavorite = mutableSetOf<TeasListener>()
    private val listenersCart = mutableListOf<TeasListener>()
    private val listenersSearch = mutableListOf<TeasListener>()

    init {
        populateTeaHome()
        populateTeaFavorite()
        populateTeaCart()

        teasSearch.addAll(teasHome)
    }

    //Главная
    fun addListenerHome(listener: TeasListener) {
        listenersHome.add(listener)
        listener.invoke(teasHome)
    }

    fun removeListenerHome(listener: TeasListener) {
        listenersHome.remove(listener)
    }

    fun moveTeaToFavorite(tea: Tea) {
        val indexToMove = teasHome.firstOrNull() { it.id == tea.id } ?: throw TeaNotFoundException()
        teasFavorite.add(indexToMove)
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
        val indexToMove = teasHome.firstOrNull() { it.id == tea.id } ?: throw TeaNotFoundException()
        teasCart.add(indexToMove)
//        notifyChangesCart()
    }

    fun deleteTeaCart(tea: Tea) {
        val indexToDelete = teasCart.indexOfFirst { it.id == tea.id }
        if (indexToDelete != -1) {
            teasCart.removeAt(indexToDelete)
            notifyChangesCart()
        }
    }

    private fun notifyChangesCart() {
        listenersCart.forEach { it.invoke(teasCart)}
    }


    //Избранное
    fun addListenerFavorite(listener: TeasListener) {
        listenersFavorite.add(listener)
        listener.invoke(teasFavorite)
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
    }

    private fun notifyChangesFavorite() {
        listenersFavorite.forEach { it.invoke(teasFavorite)}
    }


//Заполнение листов
    fun retrieveDB() {
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

        REF_DATABASE_ROOT.child(NODE_TEAS).get().addOnSuccessListener {
            var jsonString = it.value.toString().replace(" ","")
            jsonString = stringToJson(jsonString)
            val type = Types.newParameterizedType(MutableList::class.java, Tea::class.java)
            val teaListAdapter = moshi.adapter<MutableList<Tea>>(type)
            val newTeaList = teaListAdapter.fromJson(jsonString)

            for (t in newTeaList!!) {
                teasHome.add(t)
            }
//        Log.d(TAG, "retrieveDB: $teaTest")
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



    private fun populateTeaHome (){
        val tea0 = Tea(
            0,
            "https://firebasestorage.googleapis.com/v0/b/tea-quality-kotlin.appspot.com/o/teasImage%2F2.png?alt=media&token=f6f3a4b7-6237-49a2-8f83-b0650a235dcd",
            "Габа",
            "249 Р",
            "Описание Описание описание Описание Описание описание Описание Описание описаниеОписание Описание описание"
        )
        teasHome.add(tea0)
        val tea1 = Tea(
            1,
            "https://firebasestorage.googleapis.com/v0/b/tea-quality-kotlin.appspot.com/o/teasImage%2F1%2F1.png?alt=media&token=0615e24f-ad81-4d2a-9c18-a79b1337a4ea",
            "Габа",
            "249 Р",
            "Описание Описание описание Описание Описание описание Описание Описание описаниеОписание Описание описание"
        )
        teasHome.add(tea1)

        val tea2 = Tea(
            2,
            "https://firebasestorage.googleapis.com/v0/b/tea-quality-kotlin.appspot.com/o/teasImage%2F1%2F1.png?alt=media&token=0615e24f-ad81-4d2a-9c18-a79b1337a4ea",
            "Женьшеневый Улун",
            "349 Р",
            "Описание Описание описание Описание Описание описание Описание Описание описаниеОписание Описание описание"
        )
        teasHome.add(tea2)

        val tea3 = Tea(
            3,
            "https://firebasestorage.googleapis.com/v0/b/tea-quality-kotlin.appspot.com/o/teasImage%2F1%2F1.png?alt=media&token=0615e24f-ad81-4d2a-9c18-a79b1337a4ea",
            "Шу Пуэр",
            "449 Р",
            "Описание Описание описание Описание Описание описание Описание Описание описаниеОписание Описание описание"
        )
        teasHome.add(tea3)

        val tea4 = Tea(
            4,
            "https://firebasestorage.googleapis.com/v0/b/tea-quality-kotlin.appspot.com/o/teasImage%2F1%2F1.png?alt=media&token=0615e24f-ad81-4d2a-9c18-a79b1337a4ea",
            "Тегуанинь",
            "549 Р",
            "Описание Описание описание Описание Описание описание Описание Описание описаниеОписание Описание описание"
        )
        teasHome.add(tea4)

        val tea5 = Tea(
            5,
            "https://firebasestorage.googleapis.com/v0/b/tea-quality-kotlin.appspot.com/o/teasImage%2F1%2F1.png?alt=media&token=0615e24f-ad81-4d2a-9c18-a79b1337a4ea",
            "Габа",
            "249 Р",
            "Описание Описание описание Описание Описание описание Описание Описание описаниеОписание Описание описание"
        )
        teasHome.add(tea5)

        val tea6 = Tea(
            6,
            "https://firebasestorage.googleapis.com/v0/b/tea-quality-kotlin.appspot.com/o/teasImage%2F1%2F1.png?alt=media&token=0615e24f-ad81-4d2a-9c18-a79b1337a4ea",
            "Женьшеневый Улун",
            "349 Р",
            "Описание Описание описание Описание Описание описание Описание Описание описаниеОписание Описание описание"
        )
        teasHome.add(tea6)

        val tea7 = Tea(
            7,
            "https://firebasestorage.googleapis.com/v0/b/tea-quality-kotlin.appspot.com/o/teasImage%2F1%2F1.png?alt=media&token=0615e24f-ad81-4d2a-9c18-a79b1337a4ea",
            "Шу Пуэр",
            "449 Р",
            "Описание Описание описание Описание Описание описание Описание Описание описаниеОписание Описание описание"
        )
        teasHome.add(tea7)

        val tea8 = Tea(
            8,
            "https://firebasestorage.googleapis.com/v0/b/tea-quality-kotlin.appspot.com/o/teasImage%2F1%2F1.png?alt=media&token=0615e24f-ad81-4d2a-9c18-a79b1337a4ea",
            "Тегуанинь",
            "549 Р",
            "Описание Описание описание Описание Описание описание Описание Описание описаниеОписание Описание описание"
        )
        teasHome.add(tea8)
    }

    private fun populateTeaCart() {


        val tea1 = Tea(
            1,
            "https://firebasestorage.googleapis.com/v0/b/tea-quality-kotlin.appspot.com/o/teasImage%2F1%2F1.png?alt=media&token=0615e24f-ad81-4d2a-9c18-a79b1337a4ea",
            "Габа",
            "249 Р",
            "Описание Описание описание Описание Описание описание Описание Описание описаниеОписание Описание описание"
        )
        teasCart.add(tea1)
    }

    private fun populateTeaFavorite() {
        val tea1 = Tea(
            1,
            "https://firebasestorage.googleapis.com/v0/b/tea-quality-kotlin.appspot.com/o/teasImage%2F1%2F1.png?alt=media&token=0615e24f-ad81-4d2a-9c18-a79b1337a4ea",
            "Габа",
            "249 Р",
            "Описание Описание описание Описание Описание описание Описание Описание описаниеОписание Описание описание"
        )
        teasFavorite.add(tea1)

        val tea2 = Tea(
            2,
            "https://firebasestorage.googleapis.com/v0/b/tea-quality-kotlin.appspot.com/o/teasImage%2F1%2F1.png?alt=media&token=0615e24f-ad81-4d2a-9c18-a79b1337a4ea",
            "Женьшеневый Улун",
            "349 Р",
            "Описание Описание описание Описание Описание описание Описание Описание описаниеОписание Описание описание"
        )
        teasFavorite.add(tea2)
    }


}