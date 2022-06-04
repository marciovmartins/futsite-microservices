package com.github.marciovmartins.futsitev3.asm.ranking.usecase

import com.github.marciovmartins.futsitev3.asm.ranking.domain.GameDayRepository
import com.github.marciovmartins.futsitev3.asm.ranking.domain.PlayerStatistic
import com.github.marciovmartins.futsitev3.asm.ranking.domain.PlayerStatisticsRepository
import com.github.marciovmartins.futsitev3.asm.ranking.domain.PlayersStatistics
import com.github.marciovmartins.futsitev3.asm.ranking.domain.defaultProcessedGameDay
import com.github.marciovmartins.futsitev3.asm.ranking.infrastructure.FakePlayerStatisticsRepository
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.UUID

class UpdatePlayerStaticTest {
    private val gameDayRepository = mockk<GameDayRepository>()
    private val playerStatisticRepository: PlayerStatisticsRepository = FakePlayerStatisticsRepository()
    private val updatePlayerStatistic = UpdatePlayerStatic(playerStatisticRepository, gameDayRepository)

    @Test
    fun `just updating the data`() {
        // given
        val player1: UUID = UUID.randomUUID()
        val player2: UUID = UUID.randomUUID()
        val player3: UUID = UUID.randomUUID()
        val player4: UUID = UUID.randomUUID()

        val processedGameDay = defaultProcessedGameDay(player1, player2, player3, player4)
        playerStatisticRepository.persist(processedGameDay)

        val expectedPlayersStatistics = PlayersStatistics(
            matches = 1,
            items = setOf(
                PlayerStatistic(player1, 1, 1, 0, 0, 8, 3),
                PlayerStatistic(player2, 1, 1, 0, 0, 8, 3),
                PlayerStatistic(player3, 1, 0, 0, 1, 3, 8),
                PlayerStatistic(player4, 1, 1, 0, 0, 8, 3),
            ),
        )
        every { gameDayRepository.findBy(processedGameDay.gameDayId) } returns processedGameDay.copy(playersStatistics = expectedPlayersStatistics)

        // when
        val originalPlayersStatistics = playerStatisticRepository.findBy(processedGameDay.amateurSoccerGroupId)
        updatePlayerStatistic.with(processedGameDay.gameDayId)
        val updatedPlayersStatistics = playerStatisticRepository.findBy(processedGameDay.amateurSoccerGroupId)

        // then
        assertThat(updatedPlayersStatistics)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isNotEqualTo(originalPlayersStatistics)
            .isEqualTo(expectedPlayersStatistics)
    }
}