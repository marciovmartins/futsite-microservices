package com.github.marciovmartins.futsitev3.asm.ranking.usecase

import com.github.marciovmartins.futsitev3.asm.ranking.domain.GameDayRepository
import com.github.marciovmartins.futsitev3.asm.ranking.domain.PlayerStatistic
import com.github.marciovmartins.futsitev3.asm.ranking.domain.PlayersStatistics
import com.github.marciovmartins.futsitev3.asm.ranking.domain.defaultProcessedGameDay
import com.github.marciovmartins.futsitev3.asm.ranking.infrastructure.FakePlayerStatisticsRepository
import com.github.marciovmartins.futsitev3.asm.shared.domain.LocalDateInterval
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
        val player1: UUID = UUID.randomUUID()
        val player2: UUID = UUID.randomUUID()
        val player3: UUID = UUID.randomUUID()
        val player4: UUID = UUID.randomUUID()

        val interval = LocalDateInterval(LocalDate.MIN, LocalDate.MAX)

        val processedGameDay = defaultProcessedGameDay(player1, player2, player3, player4)
        every { gameDayRepository.findBy(processedGameDay.gameDayId) } returns processedGameDay

        val expectedPlayersStatistics = PlayersStatistics(
            matches = 1,
            items = setOf(
                PlayerStatistic(player1, 1, 1, 0, 0, 8, 3),
                PlayerStatistic(player2, 1, 1, 0, 0, 8, 3),
                PlayerStatistic(player3, 1, 0, 0, 1, 3, 8),
                PlayerStatistic(player4, 1, 0, 0, 1, 3, 8),
            ),
        )

        // when
        getPlayerStatistic.from(processedGameDay.gameDayId)
        val playersStatistics = playerStatisticsRepository.findBy(processedGameDay.amateurSoccerGroupId, interval)

        // then
        assertThat(playersStatistics).isEqualTo(expectedPlayersStatistics)
    }
}
