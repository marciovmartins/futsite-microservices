package com.github.marciovmartins.futsitev3.gameDay.argumentsprovider

import com.github.marciovmartins.futsitev3.gameDay.A
import com.github.marciovmartins.futsitev3.gameDay.B
import com.github.marciovmartins.futsitev3.gameDay.ExpectedException
import com.github.marciovmartins.futsitev3.gameDay.GameDayDTO
import com.github.marciovmartins.futsitev3.gameDay.GameDayFixture.gameDayDTO
import com.github.marciovmartins.futsitev3.gameDay.GameDayFixture.matchDTO
import com.github.marciovmartins.futsitev3.gameDay.GameDayFixture.playerDTO
import com.github.marciovmartins.futsitev3.gameDay.MatchDTO
import com.github.marciovmartins.futsitev3.gameDay.PlayerDTO
import org.junit.jupiter.params.provider.Arguments
import java.time.LocalDate

internal fun gameDayArgument(
    testDescription: String,
    date: Any? = LocalDate.now().toString(),
    quote: Any? = null,
    author: Any? = null,
    description: Any? = null,
    matches: Set<MatchDTO>? = setOf(matchDTO()),
    expectedException: Set<ExpectedException>? = null
) = Arguments.of(testDescription, GameDayDTO(date, quote, author, description, matches), expectedException)!!

internal fun singleMatchArgument(
    testDescription: String,
    order: Any? = 1,
    players: Set<PlayerDTO> = setOf(playerDTO(team = A), playerDTO(team = B)),
    expectedExceptions: Set<ExpectedException>? = null
): Arguments = Arguments.of(
    testDescription,
    gameDayDTO(
        matches = setOf(
            matchDTO(order = order, players = players)
        ),
    ),
    expectedExceptions
)

internal fun singleExpectedException(
    message: String,
    field: String,
) = setOf(ExpectedException(message = message, field = field))
