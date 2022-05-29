package com.github.marciovmartins.futsitev3.asm.ranking.usecase

import com.github.marciovmartins.futsitev3.asm.ranking.domain.PlayerStatistic
import com.github.marciovmartins.futsitev3.asm.ranking.domain.PlayersStatistics
import com.github.marciovmartins.futsitev3.asm.ranking.infrastructure.FakePlayerStatisticsRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import java.time.LocalDate
import java.util.UUID

class CalculateRankingTest {
    @ParameterizedTest(name = "{0}")
    @ArgumentsSource(ValidCalculateRanking::class)
    fun `with different parameters`(
        @Suppress("UNUSED_PARAMETER") testDescription: String,
        playersStatisticsByDate: Set<Pair<LocalDate, PlayerStatistic>>,
        matches: Int,
        pointsCriteria: PointCriteriaDTO,
        expectedRanking: RankingDTO
    ) {
        // given
        val amateurSoccerGroupId = UUID.randomUUID()
        val gameDayId = UUID.randomUUID()

        val playerStatisticsRepository = FakePlayerStatisticsRepository()
        val playersStatistics = playersStatisticsByDate.map { it.second }.toSet()
            .let { PlayersStatistics(matches, it) }
        playerStatisticsRepository.persist(amateurSoccerGroupId, gameDayId, LocalDate.now(), playersStatistics)

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
