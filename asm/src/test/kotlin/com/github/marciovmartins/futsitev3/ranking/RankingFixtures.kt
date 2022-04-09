package com.github.marciovmartins.futsitev3.ranking

data class RankingRequestBodyDTO(
    val period: Any?,
    val classificationMethod: Any?,
    val scoringCriteria: Any?
) {
    class Period(
        val from: Any?,
        val to: Any?
    )

    class ClassificationMethod(
        val method: Any?,
        val criteria: Any?
    ) {
        enum class Method {
            AveragePointsPerMatch,
        }

        data class Criteria(
            val percentage: Any?,
            val type: Any?
        ) {
            enum class Type {
                AverageNumberOfMatches
            }
        }
    }

    data class ScoringCriteria(
        val victories: Any?,
        val draws: Any?,
        val defeats: Any?
    )
}

data class RankingDTO(
    val players: Set<Player>
) {
    data class Player(
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