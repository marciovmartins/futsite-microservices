package com.github.marciovmartins.futsitev3.asm.ranking.infrastructure

import com.github.marciovmartins.futsitev3.asm.BaseIT
import com.github.marciovmartins.futsitev3.asm.ranking.domain.PlayerStatistic
import com.github.marciovmartins.futsitev3.asm.ranking.domain.PlayersStatistics
import com.github.marciovmartins.futsitev3.asm.ranking.domain.emptyPlayersStatistics
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate
import java.util.UUID

class JpaPlayerStatisticsRepositoryIT : BaseIT() {

    @Autowired
    lateinit var playerStatisticsRepository: JpaPlayerStatisticsRepository

    @Test
    fun `retrieve empty player statistics`() {
        // given
        val amateurSoccerGroupId = UUID.randomUUID()
        val expectedPlayersStatistics = PlayersStatistics(matches = 0, items = emptySet())

        // when
        val playersStatistics = playerStatisticsRepository.findBy(amateurSoccerGroupId)

        // then
        assertThat(playersStatistics)
            .usingRecursiveComparison()
            .isEqualTo(expectedPlayersStatistics)
    }

    @Test
    fun `persist, retrieve and delete one game day date with many player statistics`() {
        // given
        val amateurSoccerGroupId = UUID.randomUUID()
        val gameDayId = UUID.randomUUID()
        val gameDayDate = LocalDate.now()
        val playerStatisticToPersist = setOf(
            PlayerStatistic(player1, 3, 2, 1, 0, 20, 12),
            PlayerStatistic(player2, 3, 1, 1, 1, 17, 15),
            PlayerStatistic(player3, 3, 1, 1, 1, 15, 17),
            PlayerStatistic(player4, 2, 0, 0, 2, 7, 15),
            PlayerStatistic(player5, 1, 0, 1, 0, 5, 5),
        )
        val expectedPlayersStatistics = PlayersStatistics(
            matches = 3,
            items = setOf(
                PlayerStatistic(player1, 3, 2, 1, 0, 20, 12),
                PlayerStatistic(player2, 3, 1, 1, 1, 17, 15),
                PlayerStatistic(player3, 3, 1, 1, 1, 15, 17),
                PlayerStatistic(player4, 2, 0, 0, 2, 7, 15),
                PlayerStatistic(player5, 1, 0, 1, 0, 5, 5),
            )
        )

        // when
        playerStatisticsRepository.persist(
            amateurSoccerGroupId = amateurSoccerGroupId,
            gameDayId = gameDayId,
            gameDayDate = gameDayDate,
            playersStatistics = PlayersStatistics(matches = 3, playerStatisticToPersist)
        )
        val playersStatistics = playerStatisticsRepository.findBy(amateurSoccerGroupId)
        playerStatisticsRepository.delete(gameDayId)
        val playersStatisticsAfterDeletion = playerStatisticsRepository.findBy(amateurSoccerGroupId)

        // then
        assertThat(playersStatistics)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(expectedPlayersStatistics)

        assertThat(playersStatisticsAfterDeletion)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(emptyPlayersStatistics)
    }
}

private val player1 = UUID.randomUUID()
private val player2 = UUID.randomUUID()
private val player3 = UUID.randomUUID()
private val player4 = UUID.randomUUID()
private val player5 = UUID.randomUUID()
