package com.github.marciovmartins.futsitev3.ranking.infrastructure

import com.github.marciovmartins.futsitev3.ranking.domain.PlayerStatistic
import com.github.marciovmartins.futsitev3.ranking.domain.PlayerStatisticsRepository
import com.github.marciovmartins.futsitev3.ranking.domain.PlayersStatistics
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.UUID

@SpringBootTest
class PlayerStatisticsJpaRepositoryIT {

    @Autowired
    lateinit var playerStatisticsRepository: PlayerStatisticsRepository

    @Test
    fun `retrieve empty player statistics`() {
        // given
        val amateurSoccerGroupId = UUID.randomUUID()
        val expectedPlayersStatistics = PlayersStatistics(items = emptySet())

        // when
        val playersStatistics = playerStatisticsRepository.findBy(amateurSoccerGroupId)

        // then
        assertThat(playersStatistics)
            .usingRecursiveComparison()
            .isEqualTo(expectedPlayersStatistics)
    }

    @Test
    fun `retrieve many player statistics`() {
        // given
        val amateurSoccerGroupId = UUID.randomUUID()
        val playerStatisticToPersist = setOf(
            // player 1
            PlayerStatistic(player1, 1, 1, 0, 0, 8, 3),
            PlayerStatistic(player1, 1, 1, 0, 0, 7, 4),
            PlayerStatistic(player1, 1, 0, 1, 0, 5, 5),
            // player 2
            PlayerStatistic(player2, 1, 1, 0, 0, 8, 3),
            PlayerStatistic(player2, 1, 0, 0, 1, 4, 7),
            PlayerStatistic(player2, 1, 0, 1, 0, 5, 5),
            // player 3
            PlayerStatistic(player3, 1, 0, 0, 1, 3, 8),
            PlayerStatistic(player3, 1, 1, 0, 0, 7, 4),
            PlayerStatistic(player3, 1, 0, 1, 0, 5, 5),
            // player 4
            PlayerStatistic(player4, 1, 0, 0, 1, 3, 8),
            PlayerStatistic(player4, 1, 0, 0, 1, 4, 7),
            // player 5
            PlayerStatistic(player5, 1, 0, 1, 0, 5, 5),
        )
        val expectedPlayersStatistics = PlayersStatistics(
            items = setOf(
                PlayerStatistic(player1, 3, 2, 1, 0, 20, 12),
                PlayerStatistic(player2, 3, 1, 1, 1, 17, 15),
                PlayerStatistic(player3, 3, 1, 1, 1, 15, 17),
                PlayerStatistic(player4, 2, 0, 0, 2, 7, 15),
                PlayerStatistic(player5, 1, 0, 1, 0, 5, 5),
            )
        )

        // when
        playerStatisticToPersist.forEach { playerStatisticsRepository.persist(amateurSoccerGroupId, it) }
        val playersStatistics = playerStatisticsRepository.findBy(amateurSoccerGroupId)

        // then
        assertThat(playersStatistics)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(expectedPlayersStatistics)
    }
}

private val player1 = UUID.randomUUID()
private val player2 = UUID.randomUUID()
private val player3 = UUID.randomUUID()
private val player4 = UUID.randomUUID()
private val player5 = UUID.randomUUID()
