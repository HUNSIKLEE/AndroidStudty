package com.example.boardapp

import android.app.Application
import com.example.boardapp.data.ProfileDatabase

class BoardApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        ProfileDatabase.setInstance(applicationContext)
    }
}