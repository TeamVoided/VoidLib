package org.teamvoided.voidlib.woodset.block.sign

import net.minecraft.block.SignBlock
import net.minecraft.block.WoodType
import net.minecraft.util.Identifier

class VoidSignBlock(override val texture: Identifier, settings: Settings, woodType: WoodType) :
    SignBlock(settings.solid(), woodType), VoidSign

