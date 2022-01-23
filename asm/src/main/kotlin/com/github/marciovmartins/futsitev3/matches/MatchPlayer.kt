package com.github.marciovmartins.futsitev3.matches

import com.fasterxml.jackson.annotation.JsonCreator
import com.github.marciovmartins.futsitev3.EnumUtils.mapEnum
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.Max
import javax.validation.constraints.NotBlank
import javax.validation.constraints.PositiveOrZero
import javax.validation.constraints.Size

@Entity(name = "match_players")
class MatchPlayer(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Enumerated(EnumType.STRING)
    val team: Team,

    @field:NotBlank
    @field:Size(min = 1, max = 50)
    val nickname: String,

    @field:Max(255)
    @field:PositiveOrZero
    val goalsInFavor: Short,

    @field:Max(9)
    @field:PositiveOrZero
    val goalsAgainst: Short,

    val yellowCards: Short,

    val blueCards: Short,

    val redCards: Short,
) {
    enum class Team {
        A, B;

        companion object {
            @JvmStatic
            @JsonCreator
            fun createTeam(value: String): Team = mapEnum(values(), value)
        }
    }
}