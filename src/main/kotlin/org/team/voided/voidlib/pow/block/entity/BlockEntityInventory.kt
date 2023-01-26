package org.team.voided.voidlib.pow.block.entity

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.util.collection.DefaultedList

interface BlockEntityInventory : Inventory {
    val inventory: DefaultedList<ItemStack>
    fun dirty(): Boolean

    fun getItem(slot: Int): ItemStack? {
        return inventory[slot]
    }

    fun removeItem(slot: Int, amount: Int): ItemStack? {
        val stack: ItemStack = inventory[slot]
        stack.decrement(amount)
        markDirty()
        return stack
    }

    fun removeItemNoUpdate(slot: Int): ItemStack? {
        return inventory.removeAt(slot)
    }

    fun setItem(slot: Int, stack: ItemStack?) {
        inventory[slot] = stack
        markDirty()
    }

    val maxStackSize: Int
        get() = maxCountPerStack

    fun stillValid(player: PlayerEntity): Boolean {
        return true
    }

    fun clearContent() {
        inventory.clear()
    }
}