package com.github.marciovmartins.futsitev3.matches.argumentsprovider

import com.github.marciovmartins.futsitev3.MyFaker.faker
import com.github.marciovmartins.futsitev3.matches.MatchPlayer
import org.junit.jupiter.params.provider.Arguments
import java.time.LocalDate

fun matchArgument(
    description: String,
    matchDate: Any? = LocalDate.now().toString(),
    matchQuote: Any? = null,
    matchAuthor: Any? = null,
    matchDescription: Any? = null,
    matchPlayers: Set<MatchPlayerDTO>? = setOf(
        matchPlayerArgument(MatchPlayer.Team.A.name),
        matchPlayerArgument(MatchPlayer.Team.B.name),
    ),
    exceptionMessage: String? = null,
    exceptionField: String? = null,
) = Arguments.of(
    description,
    MatchDTO(matchDate, matchQuote, matchAuthor, matchDescription, matchPlayers),
    exceptionMessage,
    exceptionField
)!!

fun matchPlayerArgument(
    team: Any?,
    nickname: Any? = faker.superhero().name(),
    goalsInFavor: Any? = faker.random().nextInt(0, 5),
    goalsAgainst: Any? = faker.random().nextInt(0, 1),
    yellowCards: Any? = faker.random().nextInt(0, 2),
    blueCards: Any? = faker.random().nextInt(0, 3),
    redCards: Any? = faker.random().nextInt(0, 1),
) = MatchPlayerDTO(team, nickname, goalsInFavor, goalsAgainst, yellowCards, blueCards, redCards)

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