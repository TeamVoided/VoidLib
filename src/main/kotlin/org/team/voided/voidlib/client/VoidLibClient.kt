package org.team.voided.voidlib.client

import org.team.voided.voidlib.core.LibModule
import org.team.voided.voidlib.getModules

@Suppress("unused")
fun onInitialize() {
    getModules().forEach(LibModule::clientSetup)
}