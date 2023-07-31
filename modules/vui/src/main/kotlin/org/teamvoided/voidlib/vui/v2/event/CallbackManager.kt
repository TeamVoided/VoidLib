package org.teamvoided.voidlib.vui.v2.event

import net.minecraft.util.Identifier

abstract class CallbackManager {
    protected abstract val callbacks: MutableMap<Identifier, Callback<*>>

    protected operator fun get(id: Identifier) = callbacks[id]

    protected fun <T> getCallbackAs(id: Identifier): Callback<T> {
        var callback = callbacks[id]
        if (callback == null) {
            callback = Callback<T>()
            callbacks[id] = callback
        }

        return callback as Callback<T>
    }
}