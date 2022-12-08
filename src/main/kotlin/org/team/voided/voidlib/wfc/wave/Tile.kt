package org.team.voided.voidlib.wfc.wave

import net.minecraft.util.BlockRotation
import net.minecraft.util.Identifier
import org.team.voided.voidlib.id

data class Tile(val id: Identifier, val adjacencyIndex: AdjacencyIndex) {
    companion object {
        val EMPTY = Tile(id("empty"), AdjacencyIndex(0 ,0, 0, 0))
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