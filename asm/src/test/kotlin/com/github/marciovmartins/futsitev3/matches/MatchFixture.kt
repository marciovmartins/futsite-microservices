package com.github.marciovmartins.futsitev3.matches

import com.github.marciovmartins.futsitev3.MyFaker.faker
import java.time.LocalDate

object MatchFixture {
    fun match() = Match(
        date = LocalDate.now(),
        quote = faker.gameOfThrones().quote(),
        author = faker.gameOfThrones().character(),
        description = faker.lorem().sentence(),
    )
}
