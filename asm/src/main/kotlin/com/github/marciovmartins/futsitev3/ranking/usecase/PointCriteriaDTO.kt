package com.github.marciovmartins.futsitev3.ranking.usecase

data class PointCriteriaDTO(
    val victories: Long,
    val draws: Long,
    val defeats: Long,
    val percentage: PercentageDTO,
)

data class PercentageDTO(
    val value: Double,
    val type: PercentageType,
)

enum class PercentageType {
    /**
     * For an athlete to have his classification calculated and to be ranked, he needs to have the number of matches
     * greater than a percentage of the sum of the matches played by the participants in the period divided by the
     * number of participants in the period.
     */
    BY_AVERAGE,

    /**
     * For an athlete to be ranked, the number of matches must be greater than a percentage of the total matches played
     * in the period.
     */
    BY_TOTAL
}