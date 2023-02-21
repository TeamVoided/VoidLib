package org.teamvoided.voidlib.client

import org.teamvoided.voidlib.core.LibModule
import org.teamvoided.voidlib.getModules

@Suppress("unused")
fun onInitialize() {
    getModules().forEach(LibModule::clientSetup)
}