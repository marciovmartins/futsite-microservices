package com.github.marciovmartins.futsitev3.gameDay

import com.github.marciovmartins.futsitev3.MyFaker.faker
import java.time.LocalDate
import java.util.UUID

object GameDayFixture {
    fun gameDayDTO(
        amateurSoccerGroupId: Any? = UUID.randomUUID().toString(),
        date: Any? = LocalDate.now().toString(),
        quote: Any? = null,
        author: Any? = null,
        description: Any? = null,
        matches: Set<MatchDTO> = setOf(matchDTO())
    ) = GameDayDTO(amateurSoccerGroupId, date, quote, author, description, matches)

    fun matchDTO(
        order: Any? = 1,
        players: Set<PlayerDTO>? = setOf(playerDTO(team = A), playerDTO(team = B))
    ) = MatchDTO(order, players)

    fun playerDTO(
        team: Any?,
        userId: Any? = UUID.randomUUID().toString(),
        goalsInFavor: Any? = faker.random().nextInt(0, 5),
        goalsAgainst: Any? = faker.random().nextInt(0, 1),
        yellowCards: Any? = faker.random().nextInt(0, 2),
        blueCards: Any? = faker.random().nextInt(0, 3),
        redCards: Any? = faker.random().nextInt(0, 1),
    ) = PlayerDTO(team, userId, goalsInFavor, goalsAgainst, yellowCards, blueCards, redCards)
}

data class GameDayDTO(
    val amateurSoccerGroupId: Any?,
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

data class ExpectedResponseBody(
    val title: String,
    val status: Int,
    val violations: Set<ExpectedException>,
)

data class ExpectedException(
    val message: String,
    val field: String,
)

val A = Player.Team.A.name
val B = Player.Team.B.name