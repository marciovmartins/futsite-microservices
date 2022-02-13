package com.github.marciovmartins.futsitev3.gameDay.argumentsprovider

import com.github.marciovmartins.futsitev3.gameDay.GameDayFixture.gameDayDTO
import com.github.marciovmartins.futsitev3.gameDay.GameDayFixture.matchDTO
import com.github.marciovmartins.futsitev3.gameDay.GameDayFixture.playerDTO
import com.github.marciovmartins.futsitev3.gameDay.Player
import org.junit.jupiter.params.provider.Arguments
import java.time.LocalDate

fun gameDayArgument(
    testDescription: String,
    date: Any? = LocalDate.now().toString(),
    quote: Any? = null,
    author: Any? = null,
    description: Any? = null,
    matches: Set<MatchDTO>? = setOf(matchDTO()),
    expectedException: Set<ExpectedException>? = null
) = Arguments.of(testDescription, GameDayDTO(date, quote, author, description, matches), expectedException)!!

fun singleMatchArgument(
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

fun singleExpectedException(
    message: String,
    field: String,
) = setOf(ExpectedException(message = message, field = field))

data class ExpectedResponseBody(
    val title: String,
    val status: Int,
    val violations: Set<ExpectedException>,
)

data class ExpectedException(
    val message: String,
    val field: String,
)

data class GameDayDTO(
    val date: Any?,
    val quote: Any?,
    val author: Any?,
    val description: Any?,
    val matches: Set<MatchDTO>?
)

data class MatchDTO(
    val order: Any?,
    val players: Set<PlayerDTO>?,
)

data class PlayerDTO(
    val team: Any?,
    val userId: Any?,
    val goalsInFavor: Any?,
    val goalsAgainst: Any?,
    val yellowCards: Any?,
    val blueCards: Any?,
    val redCards: Any?,
)

val A = Player.Team.A.name
val B = Player.Team.B.name