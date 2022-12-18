package org.team.voided.voidlib.wfc.wave

import net.minecraft.util.BlockRotation
import net.minecraft.util.Identifier
import org.team.voided.voidlib.id

data class Tile(val id: Identifier, val adjacencyIndex: AdjacencyIndex, val rotatable: Boolean) {
    private var rotation = BlockRotation.NONE

    companion object {
        val EMPTY = Tile(id("empty"), AdjacencyIndex(0 ,0, 0, 0), false)
    }

    fun acceptAbove(other: Tile): Boolean {
        return other.adjacencyIndex.down == this.adjacencyIndex.up
    }

    fun acceptBelow(other: Tile): Boolean {
        return other.adjacencyIndex.up == this.adjacencyIndex.down
    }

    fun acceptLeft(other: Tile): Boolean {
        return other.adjacencyIndex.right == this.adjacencyIndex.left
    }

    fun acceptRight(other: Tile): Boolean {
        return other.adjacencyIndex.left == this.adjacencyIndex.right
    }

    private fun setRotation(rotation: BlockRotation): Tile {
        if (rotatable)
            this.rotation = rotation

        return this
    }

    fun getRotation() = rotation

    fun rotated(rotation: BlockRotation): Tile {
        if (rotatable)
            return when (rotation) {
                BlockRotation.NONE -> this
                BlockRotation.CLOCKWISE_90 -> Tile(id, AdjacencyIndex(adjacencyIndex.left, adjacencyIndex.up, adjacencyIndex.right, adjacencyIndex.down), true).setRotation(rotation)
                BlockRotation.CLOCKWISE_180 -> Tile(id, AdjacencyIndex(adjacencyIndex.down, adjacencyIndex.left, adjacencyIndex.up, adjacencyIndex.right), true).setRotation(rotation)
                BlockRotation.COUNTERCLOCKWISE_90 -> Tile(id, AdjacencyIndex(adjacencyIndex.right, adjacencyIndex.down, adjacencyIndex.left, adjacencyIndex.up), true).setRotation(rotation)
            }

        return this
    }

    override operator fun equals(other: Any?): Boolean {
        if (other !is Tile) return false

        return (other.id.namespace == this.id.namespace) && (other.id.path == this.id.namespace)
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + adjacencyIndex.hashCode()
        return result
    }
}