package org.teamvoided.voidlib.vui.v2.node

import net.minecraft.text.Text
import org.teamvoided.voidlib.core.d
import org.teamvoided.voidlib.core.datastructures.vector.Vec2i
import java.math.BigDecimal
import java.math.RoundingMode

open class DiscreteSliderNode(val min: Double, val max: Double): SliderNode() {
    var decimalPlaces = 0

    override var messageProvider: (String) -> Text = {
        Text.literal(String.format("%.${decimalPlaces}f", it))
    }

    var discreteValue
        get() = BigDecimal(this.min + this.value * (this.max - this.min)).setScale(this.decimalPlaces, RoundingMode.HALF_UP).d
        set(value) = run { slider?.void_setValue((value - min) / (max - min)) }

    constructor(pos: Vec2i, min: Double, max: Double): this(min, max) {
        this.pos = pos
    }

    constructor(pos: Vec2i, size: Vec2i, min: Double, max: Double): this(min, max) {
        this.pos = pos
        this.size = size
    }
}