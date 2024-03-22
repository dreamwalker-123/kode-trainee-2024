package com.example.kode

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class KODEApplication: Application() {

    override fun onCreate() {
        super.onCreate()
    }
}