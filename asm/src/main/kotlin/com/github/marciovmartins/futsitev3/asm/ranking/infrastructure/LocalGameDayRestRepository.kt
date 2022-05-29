package com.github.marciovmartins.futsitev3.asm.ranking.infrastructure

import com.github.marciovmartins.futsitev3.asm.gameDay.GameDayRestRepository
import com.github.marciovmartins.futsitev3.asm.ranking.domain.GameDay
import com.github.marciovmartins.futsitev3.asm.ranking.domain.GameDayDTO
import com.github.marciovmartins.futsitev3.asm.ranking.domain.GameDayRepository
import com.github.marciovmartins.futsitev3.asm.ranking.domain.MatchDTO
import com.github.marciovmartins.futsitev3.asm.ranking.domain.PlayerStatisticDTO
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.util.UUID
import com.github.marciovmartins.futsitev3.asm.gameDay.GameDay as AsmGameDay
import com.github.marciovmartins.futsitev3.asm.gameDay.Match as AsmMatch
import com.github.marciovmartins.futsitev3.asm.gameDay.PlayerStatistic as AsmPlayerStatistic

@Repository
class LocalGameDayRestRepository(
    private val gameDayRestRepository: GameDayRestRepository,
) : GameDayRepository {
    override fun findBy(gameDayId: UUID): GameDay? = gameDayRestRepository.findByIdOrNull(gameDayId)
        ?.let { GameDay(gameDayId, it.toDTO()) }
}

private fun AsmGameDay.toDTO() = GameDayDTO(
    amateurSoccerGroupId = this.amateurSoccerGroupId,
    date = this.date,
    matches = this.matches.map { it.toDTO() }.toSet()
)

private fun AsmMatch.toDTO() = MatchDTO(
    playerStatistics = this.playerStatistics.map { it.toDTO() }.toSet()
)

private fun AsmPlayerStatistic.toDTO() = PlayerStatisticDTO(
    team = PlayerStatisticDTO.Team.valueOf(this.team.name),
    playerId = this.playerId,
    goalsInFavor = this.goalsInFavor.toLong(),
    goalsAgainst = this.goalsAgainst.toLong()
)