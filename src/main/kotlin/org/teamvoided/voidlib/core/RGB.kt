package org.teamvoided.voidlib.core

data class RGB(val red: Int, val green: Int, val blue: Int) {
    fun toInt() = red and 0x0ff shl 16 or (green and 0x0ff shl 8) or (blue and 0x0ff)
}