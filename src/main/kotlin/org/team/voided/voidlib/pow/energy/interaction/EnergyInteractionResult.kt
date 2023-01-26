package org.team.voided.voidlib.pow.energy.interaction

import org.team.voided.voidlib.pow.energy.EnergyUnit
import java.math.BigDecimal

class EnergyInteractionResult(unit: EnergyUnit, originalAmount: BigDecimal, newAmount: BigDecimal, succeeded: Boolean) {
    val unit: EnergyUnit
    val originalAmount: BigDecimal
    val newAmount: BigDecimal
    val succeeded: Boolean

    init {
        this.unit = unit
        this.originalAmount = originalAmount
        this.newAmount = newAmount
        this.succeeded = succeeded
    }
}