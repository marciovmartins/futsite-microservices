package com.github.marciovmartins.futsitev3.asm.ranking.infrastructure

import com.github.marciovmartins.futsitev3.asm.BaseIT
import com.github.marciovmartins.futsitev3.asm.gameDay.GameDayRestRepository
import com.github.marciovmartins.futsitev3.asm.ranking.domain.defaultAsmGameDay
import com.github.marciovmartins.futsitev3.asm.ranking.domain.defaultGameDay
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
        val gameDayId = UUID.randomUUID()
        val amateurSoccerGroupId = UUID.randomUUID()
        val player1 = UUID.randomUUID()
        val player2 = UUID.randomUUID()
        val player3 = UUID.randomUUID()
        val player4 = UUID.randomUUID()

        val asmGameDay = defaultAsmGameDay(player1, player2, player3, player4, gameDayId, amateurSoccerGroupId)
        gameDayRestRepository.save(asmGameDay)

        val expectedGameDay = defaultGameDay(player1, player2, player3, player4, gameDayId, amateurSoccerGroupId)

        // when
        val gameDayRepository = LocalGameDayRestRepository(gameDayRestRepository)
        val gameDay = gameDayRepository.findBy(expectedGameDay.gameDayId)

        // then
        assertThat(gameDay)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(expectedGameDay)
    }
}