@file:Suppress("unused")
package org.teamvoided.voidlib.core

import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes

fun mc(path: String) = Identifier(path)
fun getId(item: Item): Identifier = Registries.ITEM.getId(item)
fun getId(block: Block): Identifier = Registries.BLOCK.getId(block)
fun rotateVoxelShape(times: Int, shape: VoxelShape): VoxelShape {
    val shapes = arrayOf(shape, VoxelShapes.empty())
    for (i in 0 until times) {
        shapes[0].forEachBox { minX, minY, minZ, maxX, maxY, maxZ ->
            shapes[1] = VoxelShapes.union(shapes[1], VoxelShapes.cuboid(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX))
        }
        shapes[0] = shapes[1]
        shapes[1] = VoxelShapes.empty()
    }
    return shapes[0]
}

