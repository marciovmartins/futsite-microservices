package com.github.marciovmartins.futsitev3.asm.ranking.domain

data class PointCriteria(
    val victories: Long,
    val draws: Long,
    val defeats: Long,
    val percentage: Percentage,
)

data class Percentage(
    val value: Double,
    val type: Type
) {
    enum class Type {
        BY_TOTAL,
        BY_AVERAGE
    }
}