package com.github.marciovmartins.futsitev3.user.data.gameDay

import com.github.marciovmartins.futsitev3.user.data.MyFaker
import org.junit.jupiter.params.provider.Arguments

fun gameDayArgument(
    testDescription: String,
    quote: Any? = MyFaker.faker.howIMetYourMother().quote().take(255),
    author: Any? = MyFaker.faker.howIMetYourMother().character().take(45),
    description: Any? = MyFaker.faker.howIMetYourMother().catchPhrase().take(2048),
): Arguments = Arguments.of(testDescription, GameDayDTO(quote, author, description))

data class GameDayDTO(
    val quote: Any?,
    val author: Any?,
    val description: Any?
)
