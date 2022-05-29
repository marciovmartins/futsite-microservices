package com.github.marciovmartins.futsitev3.asm.ranking.infrastructure

import com.github.marciovmartins.futsitev3.asm.gameDay.GameDay
import com.github.marciovmartins.futsitev3.asm.gameDay.GameDayRestRepository
import com.github.marciovmartins.futsitev3.asm.gameDay.Match
import com.github.marciovmartins.futsitev3.asm.gameDay.PlayerStatistic
import com.github.marciovmartins.futsitev3.asm.ranking.domain.GameDayDTO
import com.github.marciovmartins.futsitev3.asm.ranking.domain.GameDayRepository
import com.github.marciovmartins.futsitev3.asm.ranking.domain.MatchDTO
import com.github.marciovmartins.futsitev3.asm.ranking.domain.PlayerStatisticDTO
import com.github.marciovmartins.futsitev3.asm.ranking.domain.ProcessedGameDay
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class LocalGameDayRestRepository(
    private val gameDayRestRepository: GameDayRestRepository,
) : GameDayRepository {
    override fun findBy(gameDayId: UUID): ProcessedGameDay? = gameDayRestRepository.findByIdOrNull(gameDayId)
        ?.let { ProcessedGameDay(gameDayId, it.toDTO()) }
}

private fun GameDay.toDTO() = GameDayDTO(
    amateurSoccerGroupId = this.amateurSoccerGroupId,
    date = this.date,
    matches = this.matches.map { it.toDTO() }.toSet()
)

private fun Match.toDTO() = MatchDTO(
    playerStatistics = this.playerStatistics.map { it.toDTO() }.toSet()
)

private fun PlayerStatistic.toDTO() = PlayerStatisticDTO(
    team = PlayerStatisticDTO.Team.valueOf(this.team.name),
    playerId = this.playerId,
    goalsInFavor = this.goalsInFavor.toLong(),
    goalsAgainst = this.goalsAgainst.toLong()
)