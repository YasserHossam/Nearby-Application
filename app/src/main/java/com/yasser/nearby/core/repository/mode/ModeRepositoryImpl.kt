package com.yasser.nearby.core.repository.mode

import android.content.Context
import android.content.SharedPreferences

class ModeRepositoryImpl(context: Context) : ModeRepository {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE
    )

    companion object {
        private const val SINGLE_MODE = "Single"
        private const val REALTIME_MODE = "Realtime"

        val MODES = listOf(SINGLE_MODE, REALTIME_MODE)

        private const val SHARED_PREFERENCES_KEY = "MODE_PREFERENCES"

        private const val MODE_KEY = "nearby_mode"
    }

    override fun getModes(): List<String> {
        return MODES
    }

    override fun chooseSingleMode() {
        sharedPreferences.edit().putString(MODE_KEY, SINGLE_MODE).apply()
    }

    override fun chooseRealtimeMode() {
        sharedPreferences.edit().putString(MODE_KEY, REALTIME_MODE).apply()
    }

    override fun getCurrentMode(): String {
        return sharedPreferences.getString(MODE_KEY, null) ?: REALTIME_MODE
    }
}