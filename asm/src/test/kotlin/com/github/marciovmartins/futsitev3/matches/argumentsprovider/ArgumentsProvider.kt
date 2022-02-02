package com.github.marciovmartins.futsitev3.matches.argumentsprovider

import com.github.marciovmartins.futsitev3.matches.MatchPlayer
import com.github.marciovmartins.futsitev3.matches.MatchPlayerFixture.matchPlayerDTO
import org.junit.jupiter.params.provider.Arguments
import java.time.LocalDate

fun matchArgument(
    description: String,
    matchDate: Any? = LocalDate.now().toString(),
    matchQuote: Any? = null,
    matchAuthor: Any? = null,
    matchDescription: Any? = null,
    matchPlayers: Set<MatchPlayerDTO>? = setOf(
        matchPlayerDTO(team = A),
        matchPlayerDTO(team = B),
    ),
    expectedException: Array<ExpectedException>? = null
) = Arguments.of(
    description,
    MatchDTO(matchDate, matchQuote, matchAuthor, matchDescription, matchPlayers),
    expectedException
)!!

data class ExpectedResponseBody(
    val title: String,
    val status: Int,
    val violations: Set<ExpectedException>,
)

data class ExpectedException(
    val message: String,
    val field: String,
)

data class MatchDTO(
    val date: Any?,
    val quote: Any?,
    val author: Any?,
    val description: Any?,
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