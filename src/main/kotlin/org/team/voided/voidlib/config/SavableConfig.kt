package org.team.voided.voidlib.config

interface SavableConfig<E : SavableConfig<E>> : Config<E> {
    fun save(): E
    fun load(): E
}
