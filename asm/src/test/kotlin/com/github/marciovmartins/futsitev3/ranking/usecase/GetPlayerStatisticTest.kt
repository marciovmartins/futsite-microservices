package com.github.marciovmartins.futsitev3.ranking.usecase

import com.github.marciovmartins.futsitev3.ranking.domain.GameDay
import com.github.marciovmartins.futsitev3.ranking.domain.GameDayRepository
import com.github.marciovmartins.futsitev3.ranking.domain.Match
import com.github.marciovmartins.futsitev3.ranking.domain.PlayerStatistic
import com.github.marciovmartins.futsitev3.ranking.domain.PlayersStatistics
import com.github.marciovmartins.futsitev3.ranking.infrastructure.FakePlayerStatisticsRepository
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.UUID

class GetPlayerStatisticTest {
    private val playerStatisticsRepository = FakePlayerStatisticsRepository()
    private val gameDayRepository = mockk<GameDayRepository>()
    private val getPlayerStatistic = GetPlayerStatistic(playerStatisticsRepository, gameDayRepository)

    @Test
    fun `with a game day with one match`() {
        // given
        val gameDayId = UUID.randomUUID()
        val amateurSoccerGroupId = UUID.randomUUID()
        every { gameDayRepository.findBy(gameDayId) } returns GameDay(
            amateurSoccerGroupId = amateurSoccerGroupId,
            date = LocalDate.of(2021, 5, 1),
            matches = setOf(
                Match(
                    playerStatistics = setOf(
                        PlayerStatistic(player1, 1, 1, 0, 0, 8, 3),
                        PlayerStatistic(player2, 1, 1, 0, 0, 8, 3),
                        PlayerStatistic(player3, 1, 0, 0, 1, 3, 8),
                        PlayerStatistic(player4, 1, 0, 0, 1, 3, 8),
                    )
                )
            )
        )

        val expectedPlayersStatistics = PlayersStatistics(
            items = setOf(
                PlayerStatistic(player1, 1, 1, 0, 0, 8, 3),
                PlayerStatistic(player2, 1, 1, 0, 0, 8, 3),
                PlayerStatistic(player3, 1, 0, 0, 1, 3, 8),
                PlayerStatistic(player4, 1, 0, 0, 1, 3, 8),
            )
        )

        // when
        getPlayerStatistic.from(gameDayId)
        val playersStatistics = playerStatisticsRepository.findBy(amateurSoccerGroupId)

        // then
        assertThat(playersStatistics).isEqualTo(expectedPlayersStatistics)
    }
}

private val player1 = UUID.randomUUID()
private val player2 = UUID.randomUUID()
private val player3 = UUID.randomUUID()
private val player4 = UUID.randomUUID()
