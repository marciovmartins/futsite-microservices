package com.github.marciovmartins.futsitev3.ranking.usecase

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.UUID

class CalculateRankingTest {
    @Test
    fun `with zero game days`() {
        // given
        val amateurSoccerGroupId = UUID.randomUUID()
        val expectedRanking = TestRankingDTO(playersRanking = emptySet<TestPlayerRankingDTO>())

        // when
        val calculateRanking = CalculateRanking()
        val ranking = calculateRanking.with(amateurSoccerGroupId)

        // then
        assertThat(ranking)
            .isNotNull
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(expectedRanking)
    }
}
