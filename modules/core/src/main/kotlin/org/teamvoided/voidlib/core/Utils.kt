package org.teamvoided.voidlib.core

import java.math.BigInteger
import java.nio.ByteBuffer

const val MAXLEN = 1024

var ByteBuffer.pos
    get() = position()
    set(value) {
        position(value)
    }

fun String.trimNUL(): String {
    val nulIdx = indexOf(NUL)
    return if (nulIdx != -1) substring(0, nulIdx)
    else this
}

fun ByteBuffer.skipSpaces(): Boolean {
    var value = this[position()]
    while (value == SP.b || value == HT.b) {
        get()
        value = this[position()]
    }
    return !value.isLineEnd
}

fun ByteBuffer.skipLine(): Boolean {
    var value = this[position()]
    while (value != CR.b && value != LF.b && value != NUL.b) {
        get()
        value = this[position()]
    }
    // files are opened in binary mode. Ergo there are both NL and CR
    while (value == CR.b || value == LF.b) {
        get()
        value = this[position()]
    }
    return value != 0.b
}

fun ByteBuffer.skipSpacesAndLineEnd(): Boolean {
    var value = this[position()]
    while (value == SP.b || value == HT.b || value == CR.b || value == LF.b) {
        get()
        // check if we are at the end of file, e.g: ply
        if (remaining() > 0) value = this[position()]
        else return true
    }
    return value != 0.b
}

fun ByteBuffer.nextWord(): String {
    skipSpaces()
    val bytes = ArrayList<Byte>()
    while (!this[position()].isSpaceOrNewLine) bytes.add(get())
    return String(bytes.toByteArray())
}

fun ByteBuffer.restOfLine(): String {
    val bytes = ArrayList<Byte>()
    while (!this[position()].isLineEnd) bytes.add(get())
    return String(bytes.toByteArray())
}

fun ByteBuffer.consumeNUL() {
    while (get(pos).c == NUL) get()
}

inline val Int.b get() = toByte()

val Byte.isLineEnd get() = this == CR.b || this == LF.b || this == NUL.b || this == FF.b
inline val Byte.c get() = toInt().toChar()

val Char.isLineEnd get () = this == CR || this == LF || this == NUL || this == FF

val Byte.isSpaceOrNewLine get() = isSpace || isLineEnd
val Char.isSpaceOrNewLine get() = isSpace || isLineEnd
val Char.isNewLine get() = this == LF

val Byte.isSpace get() = this == SP.b || this == HT.b
val Char.isSpace get() = this == SP || this == HT

infix fun ByteBuffer.startsWith(string: String) = string.all { get() == it.b }

val Char.isNumeric get() = if (isDigit()) true else (this == '-' || this == '+')
inline val Char.b get() = code.toByte()

val Number.f get() = toFloat()
val Number.b get() = toByte()
val Number.d get() = toDouble()
val Number.i get() = toInt()
val Number.L get() = toLong()
val Number.s get() = toShort()
val Number.c get() = toInt().toChar()
val Number.bool get() = i != 0
val Number.str get() = toString()

operator fun Number.unaryPlus(): Double = + this.d
operator fun Number.unaryMinus(): Double = -this.d
operator fun Number.plus(number: Number): Double = this.d + number.d
operator fun Number.minus(number: Number): Double = this.d - number.d
operator fun Number.times(number: Number): Double = this.d * number.d
operator fun Number.div(number: Number): Double = this.d / number.d
operator fun Number.rem(number: Number): Double = this.d % number.d

val String.f get() = toFloat()
val String.b get() = toByte()
val String.d get() = toDouble()
val String.i get() = if (startsWith("0x")) Integer.parseInt(substring(2), 16) else toInt()
val String.L
    get() = try {
        if (startsWith("0x"))
            java.lang.Long.parseLong(substring(2), 16)
        else toLong()
    } catch (ex: NumberFormatException) {
        bi.L
    }
val String.s get() = toShort()
val String.bi get() = if (startsWith("0x")) BigInteger(substring(2), 16) else BigInteger(this)

val NUL = '\u0000'
val SOH = '\u0001'
val STX = '\u0002'
val ETX = '\u0003'
val EOT = '\u0004'
val ENQ = '\u0005'
val ACK = '\u0006'
val BEL = '\u0007'
val BS = '\u0008'
val HT = '\u0009'
val LF = '\u000A'
val VT = '\u000B'
val FF = '\u000C'
val CR = '\u000D'
val SO = '\u000E'
val SI = '\u000D'
val DLE = '\u0010'
val DC1 = '\u0011'
val DC2 = '\u0012'
val DC3 = '\u0013'
val DC4 = '\u0014'
val NAK = '\u0015'
val SYN = '\u0016'
val ETB = '\u0017'
val CAN = '\u0018'
val EM = '\u0019'
val SUB = '\u001A'
val ESC = '\u001B'
val FS = '\u001C'
val GS = '\u001D'
val RS = '\u001E'
val US = '\u001F'
val SP = '\u0020'
val DEL = '\u007F'