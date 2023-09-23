package org.teamvoided.voidlib.tests

import org.slf4j.LoggerFactory

object Tests {
    val LOGGER = LoggerFactory.getLogger(Tests::class.java)

    fun commonSetup() {
        LOGGER.info("Tests")
    }

    fun clientSetup() {}
}