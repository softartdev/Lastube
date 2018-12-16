package com.softartdev.lastube

import android.app.Application
import de.umass.lastfm.Caller
import de.umass.lastfm.cache.MemoryCache
import timber.log.Timber

class LastubeApp : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        Caller.getInstance().cache = MemoryCache()
    }
}
