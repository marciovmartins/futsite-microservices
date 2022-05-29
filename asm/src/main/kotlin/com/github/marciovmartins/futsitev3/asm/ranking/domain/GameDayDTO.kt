package com.github.marciovmartins.futsitev3.asm.ranking.domain

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate
import java.util.UUID

data class GameDayDTO(
    @field:JsonProperty("amateurSoccerGroupId") val amateurSoccerGroupId: UUID,
    @field:JsonProperty("date") val date: LocalDate,
    @field:JsonProperty("matches") val matches: Set<MatchDTO>
)

data class MatchDTO(
    @field:JsonProperty("playerStatistics") val playerStatistics: Set<PlayerStatisticDTO>,
)

data class PlayerStatisticDTO(
    @field:JsonProperty("team") val team: Team,
    @field:JsonProperty("playerId") val playerId: UUID,
    @field:JsonProperty("goalsInFavor") val goalsInFavor: Long,
    @field:JsonProperty("goalsAgainst") val goalsAgainst: Long,
) {
    enum class Team {
        A, B
    }
}