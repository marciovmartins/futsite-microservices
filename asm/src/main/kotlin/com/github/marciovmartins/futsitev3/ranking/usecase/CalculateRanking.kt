package com.github.marciovmartins.futsitev3.ranking.usecase

import com.github.marciovmartins.futsitev3.ranking.domain.GameDayRepository
import com.github.marciovmartins.futsitev3.ranking.domain.Players
import com.github.marciovmartins.futsitev3.ranking.domain.Ranking
import java.util.UUID

class CalculateRanking(private val gameDayRepository: GameDayRepository) {
    fun with(options: Options): Ranking {
        return Ranking(players = Players(emptySet())) //TODO: not implemented yet.
    }

    data class Options(val amateurSoccerGroupId: UUID)
}
