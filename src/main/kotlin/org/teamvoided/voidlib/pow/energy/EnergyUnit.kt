package org.teamvoided.voidlib.pow.energy

import net.minecraft.text.Text
import net.minecraft.util.Identifier
import org.teamvoided.voidlib.core.RGB
import java.math.BigDecimal
import java.util.*

class EnergyUnit(private val value: BigDecimal, private val id: Identifier, private val energyBarColor: RGB) {
    private val name = Text.translatable(id.namespace + ".unit." + id.path)

    fun convertTo(other: EnergyUnit, amount: BigDecimal): BigDecimal {
        return amount * (value / other.value())
    }

    fun convertFrom(other: EnergyUnit, amount: BigDecimal): BigDecimal {
        return other.convertTo(this, amount)
    }

    fun value(): BigDecimal {
        return value
    }

    fun id(): Identifier {
        return id
    }

    fun getName(): Text {
        return name
    }

    fun getEnergyBarColor(): RGB {
        return energyBarColor
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as EnergyUnit
        return value == that.value && id == that.id
    }

    override fun hashCode(): Int {
        return Objects.hash(value, id)
    }
}