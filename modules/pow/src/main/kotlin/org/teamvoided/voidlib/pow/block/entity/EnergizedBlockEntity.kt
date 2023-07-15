package org.teamvoided.voidlib.pow.block.entity

import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import org.teamvoided.voidlib.pow.energy.EnergyContainer
import org.teamvoided.voidlib.pow.energy.EnergyUnit
import org.teamvoided.voidlib.pow.energy.IEnergyContainer
import java.math.BigDecimal
import java.util.*

class EnergizedBlockEntity : BlockEntity {
    private val container: IEnergyContainer
    private val allowEnergyTransfer: MutableMap<Direction, Boolean> = EnumMap(Direction::class.java)

    constructor(
        blockEntityType: BlockEntityType<*>,
        blockPos: BlockPos,
        blockState: BlockState,
        container: IEnergyContainer,
        vararg energyTransferAllowed: Direction
    ) : super(blockEntityType, blockPos, blockState) {
        this.container = container
        defaultAllow()
        for (direction in energyTransferAllowed) {
            allowEnergyTransfer[direction] = true
        }
    }

    constructor(
        blockEntityType: BlockEntityType<*>,
        blockPos: BlockPos,
        blockState: BlockState,
        unit: EnergyUnit,
        maxCapacity: BigDecimal,
        vararg energyTransferAllowed: Direction
    ) : super(blockEntityType, blockPos, blockState) {
        container = EnergyContainer(unit, maxCapacity)
        defaultAllow()
        for (direction in energyTransferAllowed) {
            allowEnergyTransfer[direction] = true
        }
    }

    private fun defaultAllow() {
        allowEnergyTransfer[Direction.DOWN] = false
        allowEnergyTransfer[Direction.UP] = false
        allowEnergyTransfer[Direction.NORTH] = false
        allowEnergyTransfer[Direction.SOUTH] = false
        allowEnergyTransfer[Direction.WEST] = false
        allowEnergyTransfer[Direction.EAST] = false
    }

    fun energyTransferAllowedForDirection(direction: Direction): Boolean {
        return allowEnergyTransfer[direction]!!
    }

    fun getContainer(): IEnergyContainer {
        return container
    }
}