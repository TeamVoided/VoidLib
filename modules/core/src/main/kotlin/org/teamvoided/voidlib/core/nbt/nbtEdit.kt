package org.teamvoided.voidlib.core.nbt

import net.minecraft.entity.Entity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound

data class NbtEdit(val compound: NbtCompound, val action: NbtCompound.() -> Unit, val apply: () -> Unit)

fun editItemNbt(item: ItemStack, action: NbtCompound.() -> Unit): NbtEdit {
    val compound = item.orCreateNbt.copy()

    return NbtEdit(compound, action) {
        item.orCreateNbt.copyFrom(compound)
    }
}

fun ItemStack.editNbt(action: NbtCompound.() -> Unit) = editItemNbt(this, action)
fun ItemStack.editNbtAndApply(action: NbtCompound.() -> Unit) = editItemNbt(this, action).apply()

fun editEntityNbt(entity: Entity, action: NbtCompound.() -> Unit): NbtEdit {
    val compound = NbtCompound()
    entity.writeNbt(compound)

    return NbtEdit(compound, action) {
        entity.readNbt(compound)
    }
}

fun Entity.editNbt(action: NbtCompound.() -> Unit) = editEntityNbt(this, action)
fun Entity.editNbtAndApply(action: NbtCompound.() -> Unit) = editEntityNbt(this, action).apply()

fun editNbt(compound: NbtCompound, action: NbtCompound.() -> Unit): NbtEdit {
    val copy = compound.copy()
    return NbtEdit(copy, action) {
        compound.copyFrom(copy)
    }
}

fun NbtCompound.edit(action: NbtCompound.() -> Unit) = editNbt(this, action)
fun NbtCompound.editAndApply(action: NbtCompound.() -> Unit) = editNbt(this, action).apply()
