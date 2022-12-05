package org.team.voided.voidlib.config

import com.google.gson.JsonObject
import net.minecraft.text.Text

class ConfigValue private constructor() {
    private var numberVal: Number = 0
    private var stringVal: String = ""
    private var booleanVal: Boolean = false
    private var charVal: Char = '0'
    private var defNumberVal: Number = 0
    private var defStringVal: String = ""
    private var defBooleanVal: Boolean = false
    private var defCharVal: Char = '0'
    private var configType: ConfigType = ConfigType.NULL
    private var name: Text? = null
    private var toolTip: Text? = null

    constructor(toolTip: Text, number: Number): this() {
        this.toolTip = toolTip
        numberVal = number
        defNumberVal = number
        configType = ConfigType.NUMBER
    }

    constructor(toolTip: Text, string: String): this() {
        this.toolTip = toolTip
        stringVal = string
        defStringVal = string
        configType = ConfigType.STRING
    }

    constructor(toolTip: Text, boolean: Boolean): this() {
        this.toolTip = toolTip
        booleanVal = boolean
        defBooleanVal = boolean
        configType = ConfigType.BOOLEAN
    }

    constructor(toolTip: Text, char: Char): this() {
        this.toolTip = toolTip
        charVal = char
        defCharVal = char
        configType = ConfigType.CHAR
    }

    fun withName(name: Text) {
        this.name = name
    }
    fun withTooltip(toolTip: Text) {
        this.toolTip = toolTip
    }

    fun getNumberVal(): Number = numberVal

    fun getStringVal(): String = stringVal

    fun getBooleanVal(): Boolean = booleanVal

    fun getCharVal(): Char = charVal

    fun getDefNumberVal(): Number = defNumberVal

    fun getDefStringVal(): String = defStringVal

    fun getDefBooleanVal(): Boolean = defBooleanVal

    fun getDefCharVal(): Char = defCharVal

    fun getConfigType(): ConfigType = configType

    fun getName(): Text? = name

    fun getTooltip(): Text? = toolTip

    fun setNumberVal(number: Number) {
        numberVal = number
    }

    fun setStringVal(string: String) {
        stringVal = string
    }

    fun setBooleanVal(boolean: Boolean) {
        booleanVal = boolean
    }

    fun setCharVal(char: Char) {
        charVal = char
    }

    fun toJson(): JsonObject {
        val json = JsonObject()
        json.addProperty("type", configType.getNumericalType())
        when(configType) {
            ConfigType.NUMBER -> json.addProperty("val", getNumberVal())
            ConfigType.STRING -> json.addProperty("val", getStringVal())
            ConfigType.BOOLEAN -> json.addProperty("val", getBooleanVal())
            ConfigType.CHAR -> json.addProperty("val", getCharVal())
            else -> {}
        }
        json.addProperty("tooltip", Text.Serializer.toJson(toolTip ?: Text.empty()))

        return json
    }

    companion object {
        fun fromJson(json: JsonObject): ConfigValue? {
            val type: ConfigType = ConfigType.getFromNumericalType(json.get("type").asInt)
            val toolTip: Text = Text.Serializer.fromJson(json.get("tooltip").asString) ?: Text.empty()

            return when(type) {
                ConfigType.NUMBER -> ConfigValue(toolTip, json.get("val").asInt)
                ConfigType.STRING -> ConfigValue(toolTip, json.get("val").asString)
                ConfigType.BOOLEAN -> ConfigValue(toolTip, json.get("val").asBoolean)
                ConfigType.CHAR -> ConfigValue(toolTip, json.get("val").asString[0])
                else -> null
            }
        }
    }

    enum class ConfigType {
        NUMBER, STRING, BOOLEAN, CHAR, NULL;

        fun getNumericalType(): Int {
            return when(this) {
                NUMBER -> 0
                STRING -> 1
                BOOLEAN -> 2
                CHAR -> 3
                else -> -1
            }
        }

        companion object {
            fun getFromNumericalType(numericalType: Int): ConfigType {
                return when(numericalType) {
                    0 -> NUMBER
                    1 -> STRING
                    2 -> BOOLEAN
                    3 -> CHAR
                    else -> NULL
                }
            }
        }
    }
}
