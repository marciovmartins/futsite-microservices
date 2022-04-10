package com.github.marciovmartins.futsitev3.ranking

data class RankingRequestBodyDTO(
    val amateurSoccerGroupId: Any?
)

data class RankingDTO(
    val players: Set<PlayerDTO>
) {
    data class PlayerDTO(
        val position: Any?,
        val playerId: Any?,
        val classification: Any?,
        val points: Any?,
        val matches: Any?,
        val victories: Any?,
        val draws: Any?,
        val defeats: Any?,
        val goalsInFavor: Any?,
        val goalsAgainst: Any?,
        val goalsBalance: Any?
    )
}