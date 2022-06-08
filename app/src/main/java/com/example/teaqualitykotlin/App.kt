package com.example.teaqualitykotlin

import android.app.Application
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        appContext = applicationContext
        Log.d(TAG, "onCreate: ")
    }

    companion object {
        lateinit  var appContext: Context
    }

    val teasService = TeasService()
}