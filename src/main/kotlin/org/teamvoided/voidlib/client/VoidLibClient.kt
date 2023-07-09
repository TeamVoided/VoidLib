package org.teamvoided.voidlib.client

import org.teamvoided.voidlib.getModules

@Suppress("unused")
fun onInitialize() {
    getModules().forEach { libModule -> libModule.clientSetup() }
}