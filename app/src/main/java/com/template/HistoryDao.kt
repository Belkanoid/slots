package com.template

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
interface HistoryDao {

    @Query("SELECT * FROM history")
    fun getHistories() : LiveData<List<History>>

    @Insert
    fun addHistory(history : History)

}