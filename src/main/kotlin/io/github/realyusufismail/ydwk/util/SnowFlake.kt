package io.github.realyusufismail.ydwk.util

import org.jetbrains.annotations.NotNull

interface SnowFlake {
    /**
     * @return The core string of this api.
     */
    fun getId(): String {
        return getIdLong().toString()
    }

    /**
     * @return The core long of this api.
     */
    @NotNull
    fun getIdLong(): Long
}