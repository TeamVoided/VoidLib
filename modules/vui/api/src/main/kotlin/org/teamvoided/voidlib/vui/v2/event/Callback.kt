package org.teamvoided.voidlib.vui.v2.event

import java.util.LinkedList

class Callback<T> {
    private val listeners = LinkedList<Listener<T>>()

    operator fun plusAssign(listener: Listener<T>) {
        listeners += listener
    }

    operator fun minusAssign(listener: Listener<T>) {
        listeners -= listener
    }

    operator fun invoke(obj: T) = listeners.forEach { it(obj) }

    fun interface Listener<T> {
        operator fun invoke(obj: T)
    }
}