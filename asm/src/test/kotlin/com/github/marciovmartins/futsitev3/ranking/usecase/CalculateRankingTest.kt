package com.github.marciovmartins.futsitev3.ranking.usecase

import com.github.marciovmartins.futsitev3.ranking.domain.PlayerStatistics
import com.github.marciovmartins.futsitev3.ranking.infrastructure.TestDoublePlayerStatisticsRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.UUID

class CalculateRankingTest {
    @Test
    fun `with zero game days`() {
        // given
        val amateurSoccerGroupId = UUID.randomUUID()
        val playerStatisticsRepository = TestDoublePlayerStatisticsRepository()
        val expectedRanking = TestRankingDTO(playersRanking = emptySet<TestPlayerRankingDTO>())

        // when
        val calculateRanking = CalculateRanking(playerStatisticsRepository)
        val ranking = calculateRanking.with(amateurSoccerGroupId)

        // then
        assertThat(ranking)
            .isNotNull
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(expectedRanking)
    }

    @Test
    fun `with one game day with one match`() {
        // given
        val amateurSoccerGroupId = UUID.randomUUID()
        val player1 = UUID.randomUUID()
        val player2 = UUID.randomUUID()
        val player3 = UUID.randomUUID()
        val player4 = UUID.randomUUID()

        val playerStatisticsRepository = TestDoublePlayerStatisticsRepository()
        val date = LocalDate.of(2021, 5, 1)
        playerStatisticsRepository.persist(PlayerStatistics(player1, date, 1, 1, 0, 0, 8, 3))
        playerStatisticsRepository.persist(PlayerStatistics(player2, date, 1, 1, 0, 0, 8, 3))
        playerStatisticsRepository.persist(PlayerStatistics(player3, date, 1, 0, 0, 1, 3, 8))
        playerStatisticsRepository.persist(PlayerStatistics(player4, date, 1, 0, 0, 1, 3, 8))

        val expectedRanking = TestRankingDTO(
            playersRanking = setOf(
                TestPlayerRankingDTO(player1, 1, "3,000 003 1005", 3, 1, 1, 0, 0, 8, 3),
                TestPlayerRankingDTO(player2, 1, "3,000 003 1005", 3, 1, 1, 0, 0, 8, 3),
                TestPlayerRankingDTO(player3, 3, "0,000 000 0995", 0, 1, 0, 0, 1, 3, 8),
                TestPlayerRankingDTO(player4, 3, "0,000 000 0995", 0, 1, 0, 0, 1, 3, 8),
            ),
        )

        // when
        val calculateRanking = CalculateRanking(playerStatisticsRepository)
        val ranking = calculateRanking.with(amateurSoccerGroupId)

        // then
        assertThat(ranking)
            .isNotNull
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(expectedRanking)
    }
}
