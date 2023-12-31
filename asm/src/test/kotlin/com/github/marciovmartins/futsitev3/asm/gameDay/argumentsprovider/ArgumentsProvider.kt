package com.github.marciovmartins.futsitev3.asm.gameDay.argumentsprovider

import com.github.marciovmartins.futsitev3.asm.gameDay.A
import com.github.marciovmartins.futsitev3.asm.gameDay.B
import com.github.marciovmartins.futsitev3.asm.gameDay.ExpectedException
import com.github.marciovmartins.futsitev3.asm.gameDay.GameDayFixture.testGameDayDTO
import com.github.marciovmartins.futsitev3.asm.gameDay.GameDayFixture.testMatchDTO
import com.github.marciovmartins.futsitev3.asm.gameDay.GameDayFixture.testPlayerStatisticDTO
import com.github.marciovmartins.futsitev3.asm.gameDay.TestGameDayDTO
import com.github.marciovmartins.futsitev3.asm.gameDay.TestMatchDTO
import com.github.marciovmartins.futsitev3.asm.gameDay.TestPlayerStatisticDTO
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
    matches: Set<TestMatchDTO>? = setOf(testMatchDTO()),
    expectedException: Set<ExpectedException>? = null
) = Arguments.of(
    testDescription,
    TestGameDayDTO(amateurSoccerGroupId, date, quote, author, description, matches),
    expectedException
)!!

internal fun singleMatchArgument(
    testDescription: String,
    order: Any? = 1,
    playerStatistics: Set<TestPlayerStatisticDTO> = setOf(
        testPlayerStatisticDTO(team = A),
        testPlayerStatisticDTO(team = B)
    ),
    expectedExceptions: Set<ExpectedException>? = null
): Arguments = Arguments.of(
    testDescription,
    testGameDayDTO(
        matches = setOf(
            testMatchDTO(order = order, players = playerStatistics)
        ),
    ),
    expectedExceptions
)

internal fun singleExpectedException(
    message: String,
    field: String,
) = setOf(ExpectedException(message = message, field = field))
