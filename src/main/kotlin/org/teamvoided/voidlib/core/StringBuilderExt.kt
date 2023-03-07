package org.teamvoided.voidlib.core

import kotlin.text.StringBuilder

operator fun <T> StringBuilder.plusAssign(t: T) {
    append(t)
}


fun StringBuilder.safeSubstring(start: Int): String {
    return if (start == count() - 1) ""
    else substring(start)
}