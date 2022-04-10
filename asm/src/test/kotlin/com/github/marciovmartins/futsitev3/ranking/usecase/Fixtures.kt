package com.github.marciovmartins.futsitev3.ranking.usecase

data class TestRankingDTO(
    val playersRanking: Any?,
)

data class TestPlayerRankingDTO(
    val playerId: Any?,
    val position: Any?,
    val classification: Any?,
    val points: Any?,
    val matches: Any?,
    val victories: Any?,
    val draws: Any?,
    val defeats: Any?,
    val goalInFavor: Any?,
    val goalsAgainst: Any?,
)