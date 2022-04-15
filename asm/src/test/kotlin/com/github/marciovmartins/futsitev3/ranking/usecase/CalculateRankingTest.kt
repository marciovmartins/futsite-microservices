package com.github.marciovmartins.futsitev3.ranking.usecase

import com.github.marciovmartins.futsitev3.ranking.domain.PlayerStatistic
import com.github.marciovmartins.futsitev3.ranking.infrastructure.TestDoublePlayerStatisticsRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.UUID

class CalculateRankingTest {
    @ParameterizedTest(name = "{0}")
    @ArgumentsSource(ValidCalculateRanking::class)
    fun `with different parameters`(
        testDescription: String,
        playersStatistics: Set<PlayerStatistic>,
        pointsCriteria: PointCriteriaDTO,
        expectedRanking: RankingDTO
    ) {
        // given
        val amateurSoccerGroupId = UUID.randomUUID()

        val playerStatisticsRepository = TestDoublePlayerStatisticsRepository()
        playersStatistics.forEach { playerStatisticsRepository.persist(amateurSoccerGroupId, it) }

        // when
        val calculateRanking = CalculateRanking(playerStatisticsRepository)
        val ranking = calculateRanking.with(amateurSoccerGroupId, pointsCriteria)

        // then
        assertThat(ranking)
            .isNotNull
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(expectedRanking)
    }
}
