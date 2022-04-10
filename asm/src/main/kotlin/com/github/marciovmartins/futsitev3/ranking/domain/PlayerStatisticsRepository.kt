package com.github.marciovmartins.futsitev3.ranking.domain

interface PlayerStatisticsRepository {
    fun persist(playerStatistics: PlayerStatistics)
}