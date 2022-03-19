package com.github.marciovmartins.futsitev3.user.data.gameDay

import org.junit.jupiter.params.provider.Arguments

fun gameDayArgument(
    testDescription: String,
    quote: String? = null,
    author: String? = null,
    description: String? = null
): Arguments = Arguments.of(testDescription, GameDayDTO(quote, author, description))

data class GameDayDTO(
    val quote: Any?,
    val author: Any?,
    val description: Any?
)
