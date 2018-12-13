package com.softartdev.lastube

import android.app.Application
import timber.log.Timber

class LastubeApp : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
