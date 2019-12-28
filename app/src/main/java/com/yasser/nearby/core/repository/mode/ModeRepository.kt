package com.yasser.nearby.core.repository.mode

interface ModeRepository {
    fun getModes(): List<String>

    fun chooseSingleMode()

    fun chooseRealtimeMode()

    fun getCurrentMode(): String
}