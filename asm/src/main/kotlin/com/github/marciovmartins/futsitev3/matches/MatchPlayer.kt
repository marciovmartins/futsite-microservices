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
    var id: Long? = null,

    @Enumerated(EnumType.STRING)
    var team: Team,

    @field:NotBlank
    @field:Size(min = 1, max = 50)
    var nickname: String,

    @field:Max(9)
    @field:PositiveOrZero
    var goalsInFavor: Short,

    @field:Max(9)
    @field:PositiveOrZero
    var goalsAgainst: Short,

    @field:Max(9)
    @field:PositiveOrZero
    var yellowCards: Short,

    @field:Max(9)
    @field:PositiveOrZero
    var blueCards: Short,

    @field:Max(9)
    @field:PositiveOrZero
    var redCards: Short,
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