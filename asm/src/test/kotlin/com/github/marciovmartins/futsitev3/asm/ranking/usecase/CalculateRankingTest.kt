package com.github.marciovmartins.futsitev3.asm.ranking.usecase

import com.github.marciovmartins.futsitev3.asm.ranking.domain.ProcessedGameDay
import com.github.marciovmartins.futsitev3.asm.ranking.infrastructure.FakePlayerStatisticsRepository
import com.github.marciovmartins.futsitev3.asm.ranking.usecase.argumentsprovider.ValidCalculateRanking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.UUID

class CalculateRankingTest {
    @ParameterizedTest(name = "{0}")
    @ArgumentsSource(ValidCalculateRanking::class)
    fun `with different parameters`(
        @Suppress("UNUSED_PARAMETER") testDescription: String,
        amateurSoccerGroupId: UUID,
        interval: DateIntervalDTO,
        processedGameDays: Set<ProcessedGameDay>,
        pointsCriteria: PointCriteriaDTO,
        expectedRanking: RankingDTO
    ) {
        // given
        val playerStatisticsRepository = FakePlayerStatisticsRepository()
        processedGameDays.forEach { playerStatisticsRepository.persist(it) }

        // when
        val calculateRanking = CalculateRanking(playerStatisticsRepository)
        val ranking = calculateRanking.with(amateurSoccerGroupId, interval, pointsCriteria)

        // then
        assertThat(ranking)
            .isNotNull
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(expectedRanking)
    }
}
