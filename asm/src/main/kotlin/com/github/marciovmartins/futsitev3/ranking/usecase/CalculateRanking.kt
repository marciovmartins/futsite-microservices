package com.github.marciovmartins.futsitev3.ranking.usecase

import com.github.marciovmartins.futsitev3.ranking.domain.GameDayRepository
import com.github.marciovmartins.futsitev3.ranking.domain.Ranking
import java.util.UUID

class CalculateRanking(private val gameDayRepository: GameDayRepository) {
    fun with(options: Options): Ranking {
        val players = gameDayRepository.findBy(options.amateurSoccerGroupId)
        return Ranking(players)
    }

    data class Options(val amateurSoccerGroupId: UUID)
}
