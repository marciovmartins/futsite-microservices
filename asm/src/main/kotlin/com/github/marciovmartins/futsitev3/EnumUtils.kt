package com.github.marciovmartins.futsitev3

import com.github.marciovmartins.futsitev3.matches.MatchPlayer

object EnumUtils {
    @JvmStatic
    fun <T : Enum<*>> mapEnum(enum: Array<T>, value: String): T = enum.firstOrNull { it.name == value }
        ?: throw IllegalEnumArgumentException("must be one of the values accepted: [%s]".format(enumValues()), value)

    @JvmStatic
    private fun enumValues() = MatchPlayer.Team.values().joinToString(", ")
}

class IllegalEnumArgumentException(msg: String, val invalidValue: Any) : IllegalArgumentException(msg)