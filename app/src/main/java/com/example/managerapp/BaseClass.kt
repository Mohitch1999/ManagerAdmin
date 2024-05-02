package com.example.managerapp

import android.app.Application
import com.example.managerapp.utils.Prefs

class BaseClass : Application() {

    override fun onCreate() {
        super.onCreate()
        Prefs.init(this@BaseClass)
    }

}