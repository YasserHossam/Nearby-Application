package com.yasser.nearby.core.repository.mode

interface ModeRepository {
    fun chooseMode(@ApplicationMode mode: Int)

    fun getCurrentMode(): Int
}