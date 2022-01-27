package com.github.marciovmartins.futsitev3.matches

import com.github.marciovmartins.futsitev3.MyFaker.faker
import com.github.marciovmartins.futsitev3.matches.argumentsprovider.MatchDTO
import com.github.marciovmartins.futsitev3.matches.argumentsprovider.MatchPlayerDTO
import java.time.LocalDate

object MatchFixture {
    fun minimumMatchDTO() = MatchDTO(
        date = LocalDate.now().toString(),
        quote = null,
        author = null,
        description = null,
        matchPlayers = setOf(
            MatchPlayerFixture.minimumMatchPlayerDTO(team = MatchPlayer.Team.A.name),
            MatchPlayerFixture.minimumMatchPlayerDTO(team = MatchPlayer.Team.B.name),
        )
    )
}

object MatchPlayerFixture {
    fun minimumMatchPlayerDTO(team: Any?) = MatchPlayerDTO(
        team = team,
        nickname = faker.superhero().name(),
        goalsInFavor = faker.random().nextInt(0, 5),
        goalsAgainst = faker.random().nextInt(0, 1),
        yellowCards = faker.random().nextInt(0, 2),
        blueCards = faker.random().nextInt(0, 3),
        redCards = faker.random().nextInt(0, 1),
    )

    fun matchPlayerDTO(
        team: Any?,
        nickname: Any? = faker.superhero().name(),
        goalsInFavor: Any? = faker.random().nextInt(0, 5),
        goalsAgainst: Any? = faker.random().nextInt(0, 1),
        yellowCards: Any? = faker.random().nextInt(0, 2),
        blueCards: Any? = faker.random().nextInt(0, 3),
        redCards: Any? = faker.random().nextInt(0, 1),
    ) = MatchPlayerDTO(team, nickname, goalsInFavor, goalsAgainst, yellowCards, blueCards, redCards)
}
