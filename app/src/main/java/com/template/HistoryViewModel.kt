package com.template

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class HistoryViewModel : ViewModel() {
    private val historyRepository = HistoryRepository.get()
    val historyLiveData : LiveData<List<History>> = historyRepository.getHistories()
    fun addHistory(history: History){
        historyRepository.addHistory(history)
    }
}