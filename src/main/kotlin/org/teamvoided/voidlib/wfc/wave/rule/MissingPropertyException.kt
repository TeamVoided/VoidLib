package org.teamvoided.voidlib.wfc.wave.rule

import org.teamvoided.voidlib.wfc.data.JsonType

class MissingPropertyException(property: String, propertyType: JsonType) :
    RuntimeException("Missing json property with name \"$property\" of type $propertyType") {}