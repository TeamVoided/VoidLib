package org.teamvoided.voidlib.core.datastructures

import org.teamvoided.voidlib.core.d
import org.teamvoided.voidlib.core.i
import java.util.*
import kotlin.math.ceil


open class ListPartition<T>(protected val list: List<T>, val chunkSize: Int): List<List<T>> {
    override val size = ceil(list.size.d / chunkSize.d).i
    private val innerList = LinkedList<MutableList<T>>()

    companion object {
        @JvmStatic fun <T> ofSize(list: List<T>, chunkSize: Int): ListPartition<T> = ListPartition(list, chunkSize)
    }

    init {
        innerList.add(LinkedList())

        for ((counter, t) in list.withIndex()) {
            if ((counter + 1) % chunkSize == 0) {
                innerList.add(LinkedList())
            }

            innerList[innerList.size - 1].add(t)
        }
    }

    override fun get(index: Int): List<T> = innerList[index]
    override fun isEmpty(): Boolean = innerList.isEmpty()
    override fun iterator(): Iterator<List<T>> = innerList.iterator()
    override fun listIterator(): ListIterator<List<T>> = innerList.listIterator()
    override fun listIterator(index: Int): ListIterator<List<T>> = innerList.listIterator(index)
    override fun subList(fromIndex: Int, toIndex: Int): List<List<T>> = innerList.subList(fromIndex, toIndex)
    override fun lastIndexOf(element: List<T>): Int = innerList.lastIndexOf(element)
    override fun indexOf(element: List<T>): Int = innerList.indexOf(element)
    override fun containsAll(elements: Collection<List<T>>): Boolean = innerList.containsAll(elements)
    override fun contains(element: List<T>): Boolean = innerList.contains(element)
}