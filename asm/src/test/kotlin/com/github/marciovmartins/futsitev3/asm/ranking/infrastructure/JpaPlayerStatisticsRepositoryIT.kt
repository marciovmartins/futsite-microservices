package com.github.marciovmartins.futsitev3.asm.ranking.infrastructure

import com.github.marciovmartins.futsitev3.asm.BaseIT
import com.github.marciovmartins.futsitev3.asm.ranking.domain.PlayerStatistic
import com.github.marciovmartins.futsitev3.asm.ranking.domain.PlayersStatistics
import com.github.marciovmartins.futsitev3.asm.ranking.domain.ProcessedGameDay
import com.github.marciovmartins.futsitev3.asm.ranking.domain.emptyPlayersStatistics
import com.github.marciovmartins.futsitev3.asm.ranking.domain.may1st2021ProcessedGameDay
import com.github.marciovmartins.futsitev3.asm.ranking.domain.may2nd2021
import com.github.marciovmartins.futsitev3.asm.ranking.domain.may2nd2021ProcessedGameDay
import com.github.marciovmartins.futsitev3.asm.ranking.domain.may3rd2021ProcessedGameDay
import com.github.marciovmartins.futsitev3.asm.ranking.domain.may4th2021
import com.github.marciovmartins.futsitev3.asm.ranking.domain.may4th2021ProcessedGameDay
import com.github.marciovmartins.futsitev3.asm.ranking.domain.may5th2021ProcessedGameDay
import com.github.marciovmartins.futsitev3.asm.ranking.domain.player1
import com.github.marciovmartins.futsitev3.asm.ranking.domain.player2
import com.github.marciovmartins.futsitev3.asm.ranking.domain.player3
import com.github.marciovmartins.futsitev3.asm.ranking.domain.player4
import com.github.marciovmartins.futsitev3.asm.ranking.domain.player5
import com.github.marciovmartins.futsitev3.asm.shared.domain.LocalDateInterval
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
        val interval = LocalDateInterval(LocalDate.MIN, LocalDate.MAX)
        val expectedPlayersStatistics = PlayersStatistics(matches = 0, items = emptySet())

        // when
        val playersStatistics = playerStatisticsRepository.findBy(amateurSoccerGroupId, interval)

        // then
        assertThat(playersStatistics)
            .usingRecursiveComparison()
            .isEqualTo(expectedPlayersStatistics)
    }

    @Test
    fun `persist, retrieve, update and delete one game day date with many player statistics`() {
        // given
        val interval = LocalDateInterval(LocalDate.now(), LocalDate.now())
        val processedGameDay = ProcessedGameDay(
            gameDayId = UUID.randomUUID(),
            amateurSoccerGroupId = UUID.randomUUID(),
            date = LocalDate.now(),
            playersStatistics = PlayersStatistics(
                matches = 3,
                items = setOf(
                    PlayerStatistic(player1, 3, 2, 1, 0, 20, 12),
                    PlayerStatistic(player2, 3, 1, 1, 1, 17, 15),
                    PlayerStatistic(player3, 3, 1, 1, 1, 15, 17),
                    PlayerStatistic(player4, 2, 0, 0, 2, 7, 15),
                    PlayerStatistic(player5, 1, 0, 1, 0, 5, 5),
                ),
            ),
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

        val expectedUpdatedPlayersStatistics = PlayersStatistics(
            matches = 3,
            items = setOf(
                PlayerStatistic(player1, 3, 2, 1, 0, 20, 12),
                PlayerStatistic(player2, 3, 1, 1, 1, 17, 15),
                PlayerStatistic(player3, 3, 1, 1, 1, 15, 17),
                PlayerStatistic(player4, 2, 0, 0, 2, 7, 15),
                PlayerStatistic(player5, 1, 0, 1, 0, 5, 5),
            )
        )
        val processedGameDayToUpdate = processedGameDay.copy(playersStatistics = expectedUpdatedPlayersStatistics)

        // when
        playerStatisticsRepository.persist(processedGameDay)
        val playersStatistics = playerStatisticsRepository.findBy(
            processedGameDay.amateurSoccerGroupId,
            interval
        )
        playerStatisticsRepository.persist(processedGameDayToUpdate)
        val updatedPlayersStatistics = playerStatisticsRepository.findBy(
            processedGameDay.amateurSoccerGroupId,
            interval
        )
        playerStatisticsRepository.delete(processedGameDay.gameDayId)
        val playersStatisticsAfterDeletion = playerStatisticsRepository.findBy(
            processedGameDay.amateurSoccerGroupId,
            interval
        )

        // then
        assertThat(playersStatistics)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(expectedPlayersStatistics)

        assertThat(updatedPlayersStatistics)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(expectedUpdatedPlayersStatistics)

        assertThat(playersStatisticsAfterDeletion)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(emptyPlayersStatistics)
    }

    @Test
    fun `persist and retrieve game days interval`() {
        // given
        val amateurSoccerGroupId = UUID.randomUUID()
        val processedGameDays = setOf(
            may1st2021ProcessedGameDay(amateurSoccerGroupId),
            may2nd2021ProcessedGameDay(amateurSoccerGroupId),
            may3rd2021ProcessedGameDay(amateurSoccerGroupId),
            may4th2021ProcessedGameDay(amateurSoccerGroupId),
            may5th2021ProcessedGameDay(amateurSoccerGroupId),
        )

        val interval = LocalDateInterval(may2nd2021, may4th2021)

        val expectedPlayersStatistics = PlayersStatistics(
            matches = 6,
            items = setOf(
                PlayerStatistic(player1, 5, 2, 3, 0, 6, 3),
                PlayerStatistic(player2, 5, 0, 3, 2, 3, 6),
                PlayerStatistic(player3, 5, 2, 3, 0, 6, 3),
                PlayerStatistic(player4, 3, 0, 3, 0, 3, 3),
                PlayerStatistic(player5, 2, 0, 2, 0, 2, 2),
            )
        )

        // when
        processedGameDays.forEach { playerStatisticsRepository.persist(it) }
        val playersStatistics = playerStatisticsRepository.findBy(amateurSoccerGroupId, interval)

        // then
        assertThat(playersStatistics)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(expectedPlayersStatistics)
    }
}