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
    fun `with ok 200`() {
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
        val response = webTestClient.post()
            .uri("statistics/players")
            .bodyValue(rankingCriteriaDTO)
            .exchange()

        // then
        val rankingDTO = response.expectStatus().isOk
            .expectBody(RankingDTO::class.java)
            .returnResult().responseBody
        assertThat(rankingDTO)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(expectedRankingDTO)
    }

    @Test
    fun `with bad request 4xx`() {
        // given
        val expectedResponseBody = ExpectedResponseBody(
            title = "Constraint Violation",
            status = 400,
            detail = "Required request body is missing",
        )

        // when
        val response = webTestClient.post()
            .uri("statistics/players")
            .exchange()

        // then
        val actualExceptions = response.expectStatus().isBadRequest
            .expectBody(ExpectedResponseBody::class.java)
            .returnResult().responseBody
        assertThat(actualExceptions)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(expectedResponseBody)
    }

    @Test
    fun `with internal server error 5xx`() {
        // given
        val amateurSoccerGroupId = UUID.randomUUID()
        val rankingCriteriaDTO = TestRankingCriteriaDTO(
            amateurSoccerGroupId = amateurSoccerGroupId,
            pointsCriterion = TestRankingCriteriaDTO.PointsCriterionDTO(victories = 3, draws = 1, defeats = 0)
        )
        every {
            calculateRanking.with(
                amateurSoccerGroupId,
                rankingCriteriaDTO.pointsCriterion.let { PointCriteriaDTO(it.victories, it.draws, it.defeats) }
            )
        } throws Exception("Exception message")
        val expectedResponseBody = ExpectedResponseBody(
            title = "Internal Server Error",
            status = 500,
            detail = "Exception message",
        )

        // when
        val response = webTestClient.post()
            .uri("statistics/players")
            .bodyValue(rankingCriteriaDTO)
            .exchange()

        // then
        val actualExceptions = response.expectStatus().is5xxServerError
            .expectBody(ExpectedResponseBody::class.java)
            .returnResult().responseBody
        assertThat(actualExceptions)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(expectedResponseBody)
    }
}

private val player1 = UUID.randomUUID()
private val player2 = UUID.randomUUID()
private val player3 = UUID.randomUUID()
private val player4 = UUID.randomUUID()