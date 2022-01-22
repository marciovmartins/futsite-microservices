package com.github.marciovmartins.futsitev3.matches

import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity(name = "match_players")
class MatchPlayer(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Enumerated(EnumType.STRING)
    val team: Team,

    val nickname: String,

    val goalsInFavor: Short,

    val goalsAgainst: Short,

    val yellowCards: Short,

    val blueCards: Short,

    val redCards: Short,
) {
    enum class Team { A, B }
}