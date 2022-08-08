package com.template

import android.app.Application

class NoteApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        HistoryRepository.initialize(this)
    }
}