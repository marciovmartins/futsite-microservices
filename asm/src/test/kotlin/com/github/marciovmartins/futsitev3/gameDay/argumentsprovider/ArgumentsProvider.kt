package com.github.marciovmartins.futsitev3.gameDay.argumentsprovider

import com.github.marciovmartins.futsitev3.gameDay.A
import com.github.marciovmartins.futsitev3.gameDay.B
import com.github.marciovmartins.futsitev3.gameDay.ExpectedException
import com.github.marciovmartins.futsitev3.gameDay.GameDayDTO
import com.github.marciovmartins.futsitev3.gameDay.GameDayFixture.gameDayDTO
import com.github.marciovmartins.futsitev3.gameDay.GameDayFixture.matchDTO
import com.github.marciovmartins.futsitev3.gameDay.GameDayFixture.playerStatisticDTO
import com.github.marciovmartins.futsitev3.gameDay.MatchDTO
import com.github.marciovmartins.futsitev3.gameDay.PlayerStatisticDTO
import org.junit.jupiter.params.provider.Arguments
import java.time.LocalDate
import java.util.UUID

internal fun gameDayArgument(
    testDescription: String,
    amateurSoccerGroupId: Any? = UUID.randomUUID().toString(),
    date: Any? = LocalDate.now().toString(),
    quote: Any? = null,
    author: Any? = null,
    description: Any? = null,
    matches: Set<MatchDTO>? = setOf(matchDTO()),
    expectedException: Set<ExpectedException>? = null
) = Arguments.of(
    testDescription,
    GameDayDTO(amateurSoccerGroupId, date, quote, author, description, matches),
    expectedException
)!!

internal fun singleMatchArgument(
    testDescription: String,
    order: Any? = 1,
    playerStatistics: Set<PlayerStatisticDTO> = setOf(playerStatisticDTO(team = A), playerStatisticDTO(team = B)),
    expectedExceptions: Set<ExpectedException>? = null
): Arguments = Arguments.of(
    testDescription,
    gameDayDTO(
        matches = setOf(
            matchDTO(order = order, players = playerStatistics)
        ),
    ),
    expectedExceptions
)

internal fun singleExpectedException(
    message: String,
    field: String,
) = setOf(ExpectedException(message = message, field = field))
