package org.teamvoided.voidlib.vui.v2.animation

object ListExtAny {
    fun <T: List<Animation<*>>> T.forwards() {
        forEach { it.forwards() }
    }

    fun <T: List<Animation<*>>> T.backwards() {
        forEach { it.backwards() }
    }

    fun <T: List<Animation<*>>> T.reverse() {
        forEach { it.reverse() }
    }

    fun <T: List<Animation<*>>> T.loop(looping: Boolean) {
        forEach { it.loop(looping) }
    }

    fun <T: List<Animation<*>>> T.update(delta: Float) {
        forEach { it.update(delta) }
    }
}

object ListExtSpecific {
    fun <T: List<Animation<A>>, A> T.forwards() {
        forEach { it.forwards() }
    }

    fun <T: List<Animation<A>>, A> T.backwards() {
        forEach { it.backwards() }
    }

    fun <T: List<Animation<A>>, A> T.reverse() {
        forEach { it.reverse() }
    }

    fun <T: List<Animation<A>>, A> T.loop(looping: Boolean) {
        forEach { it.loop(looping) }
    }

    fun <T: List<Animation<A>>, A> T.update(delta: Float) {
        forEach { it.update(delta) }
    }
}