package org.team.voided.voidlib.fwfc.wave

import net.minecraft.util.Identifier

data class Tile(val id: Identifier, val adjacencyIndex: AdjacencyIndex) {
    companion object {
        val EMPTY = Tile(Identifier("fwfc", "empty"), AdjacencyIndex(0 ,0, 0, 0))
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