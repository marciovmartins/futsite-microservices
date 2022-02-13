package com.github.marciovmartins.futsitev3.gameDay

import com.github.marciovmartins.futsitev3.MyFaker.faker
import com.github.marciovmartins.futsitev3.gameDay.argumentsprovider.A
import com.github.marciovmartins.futsitev3.gameDay.argumentsprovider.B
import com.github.marciovmartins.futsitev3.gameDay.argumentsprovider.GameDayDTO
import com.github.marciovmartins.futsitev3.gameDay.argumentsprovider.MatchDTO
import com.github.marciovmartins.futsitev3.gameDay.argumentsprovider.PlayerDTO
import java.time.LocalDate
import java.util.UUID

object GameDayFixture {
    fun gameDayDTO(
        date: Any? = LocalDate.now().toString(),
        quote: Any? = null,
        author: Any? = null,
        description: Any? = null,
        matches: Set<MatchDTO> = setOf(matchDTO())
    ) = GameDayDTO(date, quote, author, description, matches)

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
