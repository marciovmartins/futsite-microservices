package com.github.marciovmartins.futsitev3.ranking.usecase

data class TestRankingDTO(
    val playersRanking: Any?,
)

data class TestPlayerRankingDTO(
    val playerId: Any?,
    val position: Any?,
    val classification: Any?,
    val victoryPoints: Any?,
    val matches: Any?,
    val victories: Any?,
    val draws: Any?,
    val defeats: Any?,
    val goalsInFavor: Any?,
    val goalsAgainst: Any?,
    val goalsBalance: Any?,
)