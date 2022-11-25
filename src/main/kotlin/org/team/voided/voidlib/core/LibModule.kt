package org.team.voided.voidlib.core

abstract class LibModule(val id: String) {
    abstract fun commonSetup();
    abstract fun clientSetup();
}