package org.teamvoided.voidlib.core

abstract class LibModule(val id: String) {
    abstract fun commonSetup()
    abstract fun clientSetup()
}