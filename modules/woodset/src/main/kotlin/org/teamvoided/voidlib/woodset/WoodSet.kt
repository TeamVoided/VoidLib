package org.teamvoided.voidlib.woodset

import org.slf4j.LoggerFactory
@Suppress("unused")
object WoodSet {
    private const val MOD_ID = "voidlib"

    @JvmField
    val LOGGER = LoggerFactory.getLogger(this::class.java)
    fun commonSetup() {
        LOGGER.info("WoodSet serverInit")
    }

    fun clientSetup() {
        LOGGER.info("WoodSet clientInit")
    }
}