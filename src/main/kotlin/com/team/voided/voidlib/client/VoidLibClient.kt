package com.team.voided.voidlib.client

import com.team.voided.voidlib.core.LibModule
import com.team.voided.voidlib.getModules
import java.util.*

@Suppress("unused")
fun onInitialize() {
    getModules().forEach(LibModule::clientSetup)
}