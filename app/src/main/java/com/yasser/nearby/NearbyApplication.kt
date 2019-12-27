package com.yasser.nearby

import android.app.Application
import com.yasser.nearby.core.ServiceLocator

class NearbyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        ServiceLocator.initializeController(applicationContext)
    }
}