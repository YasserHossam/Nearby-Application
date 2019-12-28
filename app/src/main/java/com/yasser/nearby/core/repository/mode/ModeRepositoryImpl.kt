package com.yasser.nearby.core.repository.mode

import android.content.Context
import android.content.SharedPreferences

class ModeRepositoryImpl(context: Context) : ModeRepository {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE
    )

    companion object {
        val MODES = listOf(ApplicationMode.SINGLE, ApplicationMode.REALTIME)

        private const val SHARED_PREFERENCES_KEY = "MODE_PREFERENCES"

        private const val MODE_KEY = "nearby_mode"
    }

    override fun chooseMode(@ApplicationMode mode: Int) {
        sharedPreferences.edit().putInt(MODE_KEY, mode).apply()
    }

    override fun getCurrentMode(): Int {
        return sharedPreferences.getInt(MODE_KEY, ApplicationMode.REALTIME)
    }
}