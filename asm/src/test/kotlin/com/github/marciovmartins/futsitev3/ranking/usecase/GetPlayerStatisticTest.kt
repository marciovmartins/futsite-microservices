package com.github.marciovmartins.futsitev3.ranking.usecase

import com.github.marciovmartins.futsitev3.ranking.domain.GameDayRepository
import com.github.marciovmartins.futsitev3.ranking.domain.PlayerStatistic
import com.github.marciovmartins.futsitev3.ranking.domain.PlayersStatistics
import com.github.marciovmartins.futsitev3.ranking.domain.defaultGameDay
import com.github.marciovmartins.futsitev3.ranking.infrastructure.FakePlayerStatisticsRepository
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.UUID

class GetPlayerStatisticTest {
    private val playerStatisticsRepository = FakePlayerStatisticsRepository()
    private val gameDayRepository = mockk<GameDayRepository>()
    private val getPlayerStatistic = GetPlayerStatistic(playerStatisticsRepository, gameDayRepository)

    @Test
    fun `with a game day with one match`() {
        // given
        val player1: UUID = UUID.randomUUID()
        val player2: UUID = UUID.randomUUID()
        val player3: UUID = UUID.randomUUID()
        val player4: UUID = UUID.randomUUID()

        val gameDay = defaultGameDay(player1, player2, player3, player4)
        every { gameDayRepository.findBy(gameDay.gameDayId) } returns gameDay

        val expectedPlayersStatistics = PlayersStatistics(
            items = setOf(
                PlayerStatistic(player1, 1, 1, 0, 0, 8, 3),
                PlayerStatistic(player2, 1, 1, 0, 0, 8, 3),
                PlayerStatistic(player3, 1, 0, 0, 1, 3, 8),
                PlayerStatistic(player4, 1, 0, 0, 1, 3, 8),
            )
        )

        // when
        getPlayerStatistic.from(gameDay.gameDayId)
        val playersStatistics = playerStatisticsRepository.findBy(gameDay.amateurSoccerGroupId)

        // then
        assertThat(playersStatistics).isEqualTo(expectedPlayersStatistics)
    }
}
