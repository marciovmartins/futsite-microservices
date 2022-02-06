package com.github.marciovmartins.futsitev3.gameDay.argumentsprovider

import com.github.marciovmartins.futsitev3.gameDay.GameDayFixture.gameDayDTO
import com.github.marciovmartins.futsitev3.gameDay.GameDayFixture.matchDTO
import com.github.marciovmartins.futsitev3.gameDay.MatchPlayer
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
    matchPlayers: Set<MatchPlayerDTO>,
    expectedExceptions: Set<ExpectedException>? = null
): Arguments = Arguments.of(
    testDescription,
    gameDayDTO(
        matches = setOf(
            matchDTO(matchPlayers = matchPlayers)
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
    val matchPlayers: Set<MatchPlayerDTO>?,
)

data class MatchPlayerDTO(
    val team: Any?,
    val nickname: Any?,
    val goalsInFavor: Any?,
    val goalsAgainst: Any?,
    val yellowCards: Any?,
    val blueCards: Any?,
    val redCards: Any?,
)

val A = MatchPlayer.Team.A.name
val B = MatchPlayer.Team.B.name