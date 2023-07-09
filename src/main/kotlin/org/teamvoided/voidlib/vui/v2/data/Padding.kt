package org.teamvoided.voidlib.vui.v2.data

data class Padding(val top: Int, val bottom: Int, val left: Int, val right: Int) {
    val horizontal get() = left + right
    val vertical get() = top + bottom

    companion object {
        val none = Padding(0, 0, 0, 0)

        fun both(horizontal: Int, vertical: Int): Padding {
            return Padding(vertical, vertical, horizontal, horizontal)
        }

        fun top(top: Int): Padding {
            return Padding(top, 0, 0, 0)
        }

        fun bottom(bottom: Int): Padding {
            return Padding(0, bottom, 0, 0)
        }

        fun left(left: Int): Padding {
            return Padding(0, 0, left, 0)
        }

        fun right(right: Int): Padding {
            return Padding(0, 0, 0, right)
        }

        fun vertical(padding: Int): Padding {
            return Padding(padding, padding, 0, 0)
        }

        fun horizontal(padding: Int): Padding {
            return Padding(0, 0, padding, padding)
        }
    }

    operator fun unaryMinus(): Padding {
        return Padding(-top, -bottom, -left, -right)
    }

    operator fun plus(padding: Padding): Padding {
        return Padding(this.top + padding.top, this.bottom + padding.bottom, this.left + padding.left, this.right + padding.right)
    }

    fun withTop(top: Int): Padding {
        return Padding(top, bottom, left, right)
    }

    fun withBottom(bottom: Int): Padding {
        return Padding(top, bottom, left, right)
    }

    fun withLeft(left: Int): Padding {
        return Padding(top, bottom, left, right)
    }

    fun withRight(right: Int): Padding {
        return Padding(top, bottom, left, right)
    }
}
