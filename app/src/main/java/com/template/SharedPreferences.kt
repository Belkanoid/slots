package com.template

import android.content.Context
import android.preference.PreferenceManager

object SharedPreferences {

    fun getCoinsAmount(context: Context): Int {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getInt("coin", 0)
    }

    fun setCoinsAmount(context: Context, maxScore : Int) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putInt("coin", maxScore)
            .apply()
    }
}