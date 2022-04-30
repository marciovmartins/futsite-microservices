package com.github.marciovmartins.futsitev3.ranking.infrastructure

import com.github.marciovmartins.futsitev3.BaseIT
import com.github.marciovmartins.futsitev3.ranking.usecase.CalculateRanking
import com.github.marciovmartins.futsitev3.ranking.usecase.PlayerRankingDTO
import com.github.marciovmartins.futsitev3.ranking.usecase.PointCriteriaDTO
import com.github.marciovmartins.futsitev3.ranking.usecase.RankingDTO
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.UUID

class CalculateRankingControllerIT : BaseIT() {
    @MockkBean
    lateinit var calculateRanking: CalculateRanking

    @Test
    fun successfully() {
        // given
        val amateurSoccerGroupId = UUID.randomUUID()
        val rankingCriteriaDTO = TestRankingCriteriaDTO(
            amateurSoccerGroupId = amateurSoccerGroupId,
            pointsCriterion = TestRankingCriteriaDTO.PointsCriterionDTO(victories = 3, draws = 1, defeats = 0)
        )
        val expectedRankingDTO = RankingDTO(
            playersRanking = setOf(
                PlayerRankingDTO(player1, 1, "3,000 003 1005", 3, 1, 1, 0, 0, 8, 3, 5),
                PlayerRankingDTO(player2, 1, "3,000 003 1005", 3, 1, 1, 0, 0, 8, 3, 5),
                PlayerRankingDTO(player3, 3, "0,000 000 0995", 0, 1, 0, 0, 1, 3, 8, -5),
                PlayerRankingDTO(player4, 3, "0,000 000 0995", 0, 1, 0, 0, 1, 3, 8, -5),
            )
        )
        every {
            calculateRanking.with(
                amateurSoccerGroupId,
                rankingCriteriaDTO.pointsCriterion.let { PointCriteriaDTO(it.victories, it.draws, it.defeats) }
            )
        } returns expectedRankingDTO

        // when
        val rankingDTO = webTestClient.post()
            .uri("statistics/players")
            .bodyValue(rankingCriteriaDTO)
            .exchange()
            .expectStatus().isOk
            .returnResult(RankingDTO::class.java)
            .responseBody.blockFirst()

        // then
        assertThat(rankingDTO)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(expectedRankingDTO)
    }
}

private val player1 = UUID.randomUUID()
private val player2 = UUID.randomUUID()
private val player3 = UUID.randomUUID()
private val player4 = UUID.randomUUID()