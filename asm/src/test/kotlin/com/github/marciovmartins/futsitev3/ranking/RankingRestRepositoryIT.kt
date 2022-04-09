package com.github.marciovmartins.futsitev3.ranking

import com.github.marciovmartins.futsitev3.BaseIT
import com.github.marciovmartins.futsitev3.gameDay.A
import com.github.marciovmartins.futsitev3.gameDay.B
import com.github.marciovmartins.futsitev3.gameDay.GameDayFixture.singleMatchGameDayDTO
import com.github.marciovmartins.futsitev3.gameDay.PlayerStatisticDTO
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.UUID

class RankingRestRepositoryIT : BaseIT() {
    @Test
    fun `calculate ranking`() {
        // setup
        val amateurSoccerGroupId = UUID.randomUUID()
        val player1 = UUID.randomUUID()
        val player2 = UUID.randomUUID()
        val player3 = UUID.randomUUID()
        val player4 = UUID.randomUUID()
        val player5 = UUID.randomUUID()
        val marcioPreto = UUID.randomUUID()

        webTestClient.put()
            .uri("gameDays/{id}", UUID.randomUUID())
            .bodyValue(
                singleMatchGameDayDTO(
                    amateurSoccerGroupId = amateurSoccerGroupId,
                    date = "2021-05-01",
                    playerStatistics = setOf(
                        PlayerStatisticDTO(A, player1, 4, 0, 0, 0, 0),
                        PlayerStatisticDTO(A, player2, 3, 0, 0, 0, 0),
                        PlayerStatisticDTO(B, player3, 2, 1, 0, 0, 0),
                        PlayerStatisticDTO(B, player4, 1, 0, 0, 0, 0),
                    ),
                )
            )
            .exchange()
            .expectStatus().isCreated
        webTestClient.put()
            .uri("gameDays/{id}", UUID.randomUUID())
            .bodyValue(
                singleMatchGameDayDTO(
                    amateurSoccerGroupId = amateurSoccerGroupId,
                    date = "2021-05-02",
                    playerStatistics = setOf(
                        PlayerStatisticDTO(A, player1, 4, 0, 0, 0, 0),
                        PlayerStatisticDTO(A, player3, 3, 1, 0, 0, 0),
                        PlayerStatisticDTO(B, player2, 1, 0, 0, 0, 0),
                        PlayerStatisticDTO(B, player4, 2, 0, 0, 0, 0),
                    ),
                )
            )
            .exchange()
            .expectStatus().isCreated
        webTestClient.put()
            .uri("gameDays/{id}", UUID.randomUUID())
            .bodyValue(
                singleMatchGameDayDTO(
                    amateurSoccerGroupId = amateurSoccerGroupId,
                    date = "2021-05-03",
                    playerStatistics = setOf(
                        PlayerStatisticDTO(A, player1, 2, 0, 0, 0, 0),
                        PlayerStatisticDTO(A, player5, 3, 0, 0, 0, 0),
                        PlayerStatisticDTO(B, player2, 1, 0, 0, 0, 0),
                        PlayerStatisticDTO(B, player3, 4, 0, 0, 0, 0),
                    ),
                ),
            )
            .exchange()
            .expectStatus().isCreated

        val requestBody = RankingRequestBodyDTO(
            amateurSoccerGroupId = amateurSoccerGroupId
        )

        val expectedRanking = RankingDTO(
            players = setOf(
                RankingDTO.Player(
                    position = 1,
                    playerId = player1,
                    classification = "2,333 006 1008",
                    points = 7,
                    matches = 3,
                    victories = 2,
                    draws = 1,
                    defeats = 0,
                    goalsInFavor = 20,
                    goalsAgainst = 12,
                    goalsBalance = 8,
                ),
                RankingDTO.Player(
                    position = 2,
                    playerId = player2,
                    classification = "1,333 003 1002",
                    points = 4,
                    matches = 3,
                    victories = 1,
                    draws = 1,
                    defeats = 1,
                    goalsInFavor = 17,
                    goalsAgainst = 15,
                    goalsBalance = 2,
                ),
                RankingDTO.Player(
                    position = 3,
                    playerId = player3,
                    classification = "1,333 003 0998",
                    points = 4,
                    matches = 3,
                    victories = 1,
                    draws = 1,
                    defeats = 1,
                    goalsInFavor = 15,
                    goalsAgainst = 17,
                    goalsBalance = -2,
                ),
                RankingDTO.Player(
                    position = null,
                    playerId = marcioPreto,
                    classification = null,
                    points = 0,
                    matches = 0,
                    victories = 0,
                    draws = 0,
                    defeats = 0,
                    goalsInFavor = 0,
                    goalsAgainst = 0,
                    goalsBalance = 0,
                ),
                RankingDTO.Player(
                    position = null,
                    playerId = player5,
                    classification = null,
                    points = 1,
                    matches = 1,
                    victories = 0,
                    draws = 1,
                    defeats = 0,
                    goalsInFavor = 5,
                    goalsAgainst = 5,
                    goalsBalance = 0,
                ),
                RankingDTO.Player(
                    position = null,
                    playerId = player4,
                    classification = null,
                    points = 0,
                    matches = 2,
                    victories = 0,
                    draws = 0,
                    defeats = 2,
                    goalsInFavor = 7,
                    goalsAgainst = 15,
                    goalsBalance = -8,
                ),
            ),
        )

        // execution
        val response = webTestClient.post()
            .uri("rankings")
            .bodyValue(requestBody)
            .exchange()

        // assertions
        response.expectStatus().isOk
        val ranking = response.returnResult(RankingDTO::class.java).responseBody.blockFirst()
        assertThat(ranking)
            .isNotNull
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(expectedRanking)
    }
}