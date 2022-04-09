package com.github.marciovmartins.futsitev3.ranking.usecase

import com.github.marciovmartins.futsitev3.ranking.domain.GameDayRepository
import com.github.marciovmartins.futsitev3.ranking.domain.Players
import com.github.marciovmartins.futsitev3.ranking.domain.Ranking
import java.util.UUID

class CalculateRanking(gameDayRepository: GameDayRepository) {
    fun with(options: Options): Ranking {
        return Ranking(Players(emptySet())) //TODO: Not yet implemented
    }

    data class Options(val amateurSoccerGroupId: UUID)
}
