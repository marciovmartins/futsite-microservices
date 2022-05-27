package com.github.marciovmartins.futsitev3.gameDay

import com.github.marciovmartins.futsitev3.MyFaker.faker
import java.time.LocalDate
import java.util.UUID

object GameDayFixture {
    fun testGameDayDTO(
        amateurSoccerGroupId: Any? = UUID.randomUUID().toString(),
        date: Any? = LocalDate.now().toString(),
        quote: Any? = null,
        author: Any? = null,
        description: Any? = null,
        matches: Set<TestMatchDTO> = setOf(testMatchDTO())
    ) = TestGameDayDTO(amateurSoccerGroupId, date, quote, author, description, matches)

    fun testMatchDTO(
        order: Any? = 1,
        players: Set<TestPlayerStatisticDTO>? = setOf(
            testPlayerStatisticDTO(team = A),
            testPlayerStatisticDTO(team = B)
        )
    ) = TestMatchDTO(order, players)

    fun testPlayerStatisticDTO(
        team: Any?,
        playerId: Any? = UUID.randomUUID().toString(),
        goalsInFavor: Any? = faker.random().nextInt(0, 5),
        goalsAgainst: Any? = faker.random().nextInt(0, 1),
        yellowCards: Any? = faker.random().nextInt(0, 2),
        blueCards: Any? = faker.random().nextInt(0, 3),
        redCards: Any? = faker.random().nextInt(0, 1),
    ) = TestPlayerStatisticDTO(team, playerId, goalsInFavor, goalsAgainst, yellowCards, blueCards, redCards)
}

data class TestGameDayDTO(
    val amateurSoccerGroupId: Any?,
    val date: Any?,
    val quote: Any?,
    val author: Any?,
    val description: Any?,
    val matches: Set<TestMatchDTO>?
)

data class TestMatchDTO(
    val order: Any?,
    val playerStatistics: Set<TestPlayerStatisticDTO>?,
)

data class TestPlayerStatisticDTO(
    val team: Any?,
    val playerId: Any?,
    val goalsInFavor: Any?,
    val goalsAgainst: Any?,
    val yellowCards: Any?,
    val blueCards: Any?,
    val redCards: Any?,
)

data class TestGameDayEvent(
    val gameDayId: String,
)

data class GameDayCollection(
    val _embedded: EmbeddedGameDays,
    val _links: Links,
    val page: Page,
) {
    data class EmbeddedGameDays(
        val gameDays: List<TestGameDayDTO>,
    )
}

data class Links(
    val self: Link,
) {
    data class Link(
        val href: String,
    )
}

data class Page(
    val size: Long,
    val totalElements: Long,
    val totalPages: Long,
    val number: Long
)

data class ExpectedResponseBody(
    val title: String,
    val status: Int,
    val violations: Set<ExpectedException>? = null,
    val detail: String? = null,
)

data class ExpectedException(
    val message: String,
    val field: String? = null,
)

val A = PlayerStatistic.Team.A.name
val B = PlayerStatistic.Team.B.name