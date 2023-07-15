package org.teamvoided.voidlib.core

fun String.isNumeric(): Boolean {
    val integerChars = '0'..'9'
    var dotOccurred = 0
    return this.all { it in integerChars || it == '.' && dotOccurred++ < 1 }
}