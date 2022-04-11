package com.github.marciovmartins.futsitev3.ranking.usecase

import com.github.marciovmartins.futsitev3.ranking.domain.PlayerStatistic
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
        playerStatisticsRepository.persist(amateurSoccerGroupId, PlayerStatistic(player1, date, 1, 1, 0, 0, 8, 3))
        playerStatisticsRepository.persist(amateurSoccerGroupId, PlayerStatistic(player2, date, 1, 1, 0, 0, 8, 3))
        playerStatisticsRepository.persist(amateurSoccerGroupId, PlayerStatistic(player3, date, 1, 0, 0, 1, 3, 8))
        playerStatisticsRepository.persist(amateurSoccerGroupId, PlayerStatistic(player4, date, 1, 0, 0, 1, 3, 8))

        val expectedRanking = TestRankingDTO(
            playersRanking = setOf(
                TestPlayerRankingDTO(player1, 1, "3,000 003 1005", 3, 1, 1, 0, 0, 8, 3, 5),
                TestPlayerRankingDTO(player2, 1, "3,000 003 1005", 3, 1, 1, 0, 0, 8, 3, 5),
                TestPlayerRankingDTO(player3, 3, "0,000 000 0995", 0, 1, 0, 0, 1, 3, 8, -5),
                TestPlayerRankingDTO(player4, 3, "0,000 000 0995", 0, 1, 0, 0, 1, 3, 8, -5),
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

    @Test
    fun `with many game days with one match each`() {
        // given
        val amateurSoccerGroupId = UUID.randomUUID()
        val player1 = UUID.randomUUID()
        val player2 = UUID.randomUUID()
        val player3 = UUID.randomUUID()
        val player4 = UUID.randomUUID()
        val player5 = UUID.randomUUID()

        val playerStatisticsRepository = TestDoublePlayerStatisticsRepository()
        val date1 = LocalDate.of(2021, 5, 1)
        playerStatisticsRepository.persist(amateurSoccerGroupId, PlayerStatistic(player1, date1, 1, 1, 0, 0, 8, 3))
        playerStatisticsRepository.persist(amateurSoccerGroupId, PlayerStatistic(player2, date1, 1, 1, 0, 0, 8, 3))
        playerStatisticsRepository.persist(amateurSoccerGroupId, PlayerStatistic(player3, date1, 1, 0, 0, 1, 3, 8))
        playerStatisticsRepository.persist(amateurSoccerGroupId, PlayerStatistic(player4, date1, 1, 0, 0, 1, 3, 8))
        val date2 = LocalDate.of(2021, 5, 2)
        playerStatisticsRepository.persist(amateurSoccerGroupId, PlayerStatistic(player1, date2, 1, 1, 0, 0, 7, 4))
        playerStatisticsRepository.persist(amateurSoccerGroupId, PlayerStatistic(player3, date2, 1, 1, 0, 0, 7, 4))
        playerStatisticsRepository.persist(amateurSoccerGroupId, PlayerStatistic(player2, date2, 1, 0, 0, 1, 4, 7))
        playerStatisticsRepository.persist(amateurSoccerGroupId, PlayerStatistic(player4, date2, 1, 0, 0, 1, 4, 7))
        val date3 = LocalDate.of(2021, 5, 3)
        playerStatisticsRepository.persist(amateurSoccerGroupId, PlayerStatistic(player1, date3, 1, 0, 1, 0, 5, 5))
        playerStatisticsRepository.persist(amateurSoccerGroupId, PlayerStatistic(player5, date3, 1, 0, 1, 0, 5, 5))
        playerStatisticsRepository.persist(amateurSoccerGroupId, PlayerStatistic(player2, date3, 1, 0, 1, 0, 5, 5))
        playerStatisticsRepository.persist(amateurSoccerGroupId, PlayerStatistic(player3, date3, 1, 0, 1, 0, 5, 5))

        val expectedRanking = TestRankingDTO(
            playersRanking = setOf(
                TestPlayerRankingDTO(player1, 1, "2,333 006 1008", 7, 3, 2, 1, 0, 20, 12, 8),
                TestPlayerRankingDTO(player2, 2, "1,333 003 1002", 4, 3, 1, 1, 1, 17, 15, 2),
                TestPlayerRankingDTO(player3, 3, "1,333 003 0998", 4, 3, 1, 1, 1, 15, 17, -2),
                TestPlayerRankingDTO(player5, 4, "1,000 000 1000", 1, 1, 0, 1, 0, 5, 5, 0),
                TestPlayerRankingDTO(player4, 5, "0,000 000 0992", 0, 2, 0, 0, 2, 7, 15, -8),
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
