package com.github.marciovmartins.futsitev3.matches

import java.time.LocalDate
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToMany
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.PastOrPresent
import javax.validation.constraints.Size

@Entity(name = "matches")
class Match(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @field:PastOrPresent
    var date: LocalDate,

    @field:Size(max = 255)
    var quote: String?,

    @field:Size(max = 50)
    var author: String?,

    @field:Size(max = 2048)
    var description: String?,

    @field:NotEmpty
    @field:BothTeams
    @JoinColumn(name = "match_id", nullable = false)
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    var matchPlayers: Set<MatchPlayer>
)
