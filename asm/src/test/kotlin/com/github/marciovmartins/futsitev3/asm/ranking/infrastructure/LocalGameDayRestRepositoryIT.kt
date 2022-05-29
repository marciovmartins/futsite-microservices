package com.github.marciovmartins.futsitev3.asm.ranking.infrastructure

import com.github.marciovmartins.futsitev3.asm.BaseIT
import com.github.marciovmartins.futsitev3.asm.gameDay.GameDayRestRepository
import com.github.marciovmartins.futsitev3.asm.ranking.domain.defaultGameDay
import com.github.marciovmartins.futsitev3.asm.ranking.domain.defaultProcessedGameDay
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.UUID


class LocalGameDayRestRepositoryIT : BaseIT() {
    @Autowired
    private lateinit var gameDayRestRepository: GameDayRestRepository

    @Test
    fun `retrieve game day from GameDayRestRepository`() {
        // given
        val player1 = UUID.randomUUID()
        val player2 = UUID.randomUUID()
        val player3 = UUID.randomUUID()
        val player4 = UUID.randomUUID()

        val gameDay = defaultGameDay(player1, player2, player3, player4)
        gameDayRestRepository.save(gameDay)

        val expectedProcessedGameDay = defaultProcessedGameDay(
            player1 = player1,
            player2 = player2,
            player3 = player3,
            player4 = player4,
            gameDayId = gameDay.id!!,
            amateurSoccerGroupId = gameDay.amateurSoccerGroupId
        )

        // when
        val gameDayRepository = LocalGameDayRestRepository(gameDayRestRepository)
        val processedGameDay = gameDayRepository.findBy(expectedProcessedGameDay.gameDayId)

        // then
        assertThat(processedGameDay)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(expectedProcessedGameDay)
    }
}