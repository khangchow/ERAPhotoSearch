package com.era.photosearch.presentation

import android.app.Application
import com.era.photosearch.BuildConfig
import com.era.photosearch.util.DebuggingActivityLifecycleCallback
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            registerActivityLifecycleCallbacks(DebuggingActivityLifecycleCallback())
        }
    }
}