package org.teamvoided.voidlib.pow.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.stat.Stats
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import org.teamvoided.voidlib.pow.block.entity.EnergizedBlockEntity
import org.teamvoided.voidlib.pow.item.IEnergizedItem

abstract class EnergizedBlock(properties: Settings?) : Block(properties) {
    override fun onBreak(
        world: World,
        pos: BlockPos,
        state: BlockState,
        player: PlayerEntity,
    ) {
        val blockEntity = if (state.hasBlockEntity()) world.getBlockEntity(pos) else null

        val item = state.block.asItem()

        if (shouldDropWithEnergy() && (blockEntity is EnergizedBlockEntity) && (item is IEnergizedItem)
        ) {
            val replace = ItemStack(state.block, 1)
            player.incrementStat(Stats.MINED.getOrCreateStat(this))
            player.hungerManager.addExhaustion(0.005f)
            item.setEnergy(replace, blockEntity.getContainer().stored())
            dropStack(world, pos, replace)
            return
        }
        super.onBreak(world, pos, state, player)
    }

    protected abstract fun shouldDropWithEnergy(): Boolean
}