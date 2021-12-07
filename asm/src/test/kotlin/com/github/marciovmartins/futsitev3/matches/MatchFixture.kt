package com.github.marciovmartins.futsitev3.matches

import com.github.marciovmartins.futsitev3.MyFaker.faker
import java.time.LocalDate
import java.util.UUID

object MatchFixture {
    fun match() = Match(
        id = UUID.randomUUID(),
        date = LocalDate.now(),
        quote = faker.gameOfThrones().quote(),
        author = faker.gameOfThrones().character(),
        description = faker.lorem().sentence(),
    )
}
