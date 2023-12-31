package com.github.marciovmartins.futsitev3.asm.ranking.infrastructure

import com.github.marciovmartins.futsitev3.asm.BaseIT
import com.github.marciovmartins.futsitev3.asm.ranking.usecase.CalculateRanking
import com.github.marciovmartins.futsitev3.asm.ranking.usecase.DateIntervalDTO
import com.github.marciovmartins.futsitev3.asm.ranking.usecase.PercentageDTO
import com.github.marciovmartins.futsitev3.asm.ranking.usecase.PercentageType
import com.github.marciovmartins.futsitev3.asm.ranking.usecase.PlayerRankingDTO
import com.github.marciovmartins.futsitev3.asm.ranking.usecase.PointCriteriaDTO
import com.github.marciovmartins.futsitev3.asm.ranking.usecase.RankingDTO
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.UUID

class CalculateRankingRestControllerIT : BaseIT() {
    @MockkBean
    lateinit var calculateRanking: CalculateRanking

    @Test
    fun `with ok 200`() {
        // given
        val amateurSoccerGroupId = UUID.randomUUID()
        val interval = TestRankingCriteriaDTO.TestLocalDateInterval(
            from = LocalDate.now(),
            to = LocalDate.now(),
        )
        val rankingCriteriaDTO = TestRankingCriteriaDTO(
            amateurSoccerGroupId = amateurSoccerGroupId,
            interval = interval,
            pointsCriterion = TestRankingCriteriaDTO.PointsCriterionDTO(victories = 3, draws = 1, defeats = 0)
        )
        val expectedRankingDTO = RankingDTO(
            playersRanking = setOf(
                PlayerRankingDTO(player1, 1, "3,000 003 1005", 3, 1, 1, 0, 0, 8, 3, 5),
                PlayerRankingDTO(player2, 1, "3,000 003 1005", 3, 1, 1, 0, 0, 8, 3, 5),
                PlayerRankingDTO(player3, 3, "0,000 000 0995", 0, 1, 0, 0, 1, 3, 8, -5),
                PlayerRankingDTO(player4, 3, "0,000 000 0995", 0, 1, 0, 0, 1, 3, 8, -5),
            ),
            minimumMatches = 0.1,
        )
        every {
            calculateRanking.with(
                amateurSoccerGroupId,
                DateIntervalDTO(interval.from, interval.to),
                rankingCriteriaDTO.pointsCriterion.let {
                    PointCriteriaDTO(
                        victories = it.victories,
                        draws = it.draws,
                        defeats = it.defeats,
                        percentage = it.percentage.let { it2 ->
                            PercentageDTO(
                                value = it2.value,
                                type = PercentageType.valueOf(it2.type)
                            )
                        })
                }
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
        val dateInterval = TestRankingCriteriaDTO.TestLocalDateInterval(LocalDate.now(), LocalDate.now())
        val rankingCriteriaDTO = TestRankingCriteriaDTO(
            amateurSoccerGroupId = amateurSoccerGroupId,
            interval = dateInterval
        )
        every {
            calculateRanking.with(
                amateurSoccerGroupId,
                DateIntervalDTO(dateInterval.from, dateInterval.to),
                rankingCriteriaDTO.pointsCriterion.let {
                    PointCriteriaDTO(
                        victories = it.victories,
                        draws = it.draws,
                        defeats = it.defeats,
                        percentage = it.percentage.let { it2 ->
                            PercentageDTO(
                                value = it2.value,
                                type = PercentageType.valueOf(it2.type)
                            )
                        })
                }
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