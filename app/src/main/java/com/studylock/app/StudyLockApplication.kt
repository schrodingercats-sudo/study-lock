package com.studylock.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class StudyLockApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}
