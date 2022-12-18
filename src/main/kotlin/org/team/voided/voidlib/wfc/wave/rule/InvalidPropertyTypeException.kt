package org.team.voided.voidlib.wfc.wave.rule

import org.team.voided.voidlib.wfc.data.JsonType

class InvalidPropertyTypeException(property: String, expectedType: JsonType, found: JsonType):
    RuntimeException("Invalid property type found at property \"$property\", found $found, expected $expectedType") {}