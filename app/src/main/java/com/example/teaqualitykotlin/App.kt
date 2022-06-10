package com.example.teaqualitykotlin

import android.app.Application
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log

class App : Application() {

    val teasService = TeasService()
}