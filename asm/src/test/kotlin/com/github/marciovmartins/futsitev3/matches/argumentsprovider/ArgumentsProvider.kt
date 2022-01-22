package com.github.marciovmartins.futsitev3.matches.argumentsprovider

import com.github.marciovmartins.futsitev3.MyFaker.faker
import com.github.marciovmartins.futsitev3.matches.MatchPlayer
import org.junit.jupiter.params.provider.Arguments
import java.time.LocalDate

fun matchArgument(
    description: String,
    matchDate: LocalDate? = LocalDate.now(),
    matchQuote: String? = null,
    matchAuthor: String? = null,
    matchDescription: String? = null,
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
    team: String?,
    nickname: String? = faker.superhero().name(),
    goalsInFavor: Int? = faker.random().nextInt(0, 5),
    goalsAgainst: Int? = faker.random().nextInt(0, 1),
    yellowCards: Int? = faker.random().nextInt(0, 2),
    blueCards: Int? = faker.random().nextInt(0, 3),
    redCards: Int? = faker.random().nextInt(0, 1),
) = MatchPlayerDTO(team, nickname, goalsInFavor, goalsAgainst, yellowCards, blueCards, redCards)

data class MatchDTO(
    val date: LocalDate?,
    val quote: String?,
    val author: String?,
    val description: String?,
    val matchPlayers: Set<MatchPlayerDTO>?,
)

data class MatchPlayerDTO(
    val team: String?,
    val nickname: String?,
    val goalsInFavor: Int?,
    val goalsAgainst: Int?,
    val yellowCards: Int?,
    val blueCards: Int?,
    val redCards: Int?,
)

val A = MatchPlayer.Team.A.name
val B = MatchPlayer.Team.B.name