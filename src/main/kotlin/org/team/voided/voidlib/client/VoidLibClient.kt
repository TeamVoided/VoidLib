package org.team.voided.voidlib.client

import org.team.voided.voidlib.core.LibModule
import org.team.voided.voidlib.getModules
import java.util.*

@Suppress("unused")
fun onInitialize() {
    getModules().forEach(LibModule::clientSetup)
}