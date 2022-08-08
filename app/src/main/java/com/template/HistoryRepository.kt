package com.template

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import java.util.concurrent.Executors

class HistoryRepository private constructor(context: Context) {

    private val database : HistoryDatabase =
        Room.databaseBuilder(
            context.applicationContext,
        HistoryDatabase::class.java,
        "HistoryDB")
            .build()


    private val historyDao = database.HistoryDao()
    private val executor = Executors.newSingleThreadExecutor()


    fun getHistories() : LiveData<List<History>> = historyDao.getHistories()


    fun addHistory(history: History) {
        executor.execute {
            historyDao.addHistory(history)
        }
    }



    companion object {
        private var INSTANCE : HistoryRepository? = null
        fun initialize(context : Context) {
            if (INSTANCE == null) {
                INSTANCE = HistoryRepository(context)
            }
        }
        fun get() : HistoryRepository {
            return INSTANCE ?:
            throw
            IllegalStateException("HistoryRepository must me initialized")
        }
    }
}