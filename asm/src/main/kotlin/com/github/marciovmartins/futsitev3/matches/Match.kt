package com.github.marciovmartins.futsitev3.matches

import java.time.LocalDate
import javax.persistence.CascadeType
import javax.persistence.Column
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
    val id: Long? = null,

    @field:PastOrPresent
    @Column(nullable = false)
    val date: LocalDate,

    @field:Size(max = 255)
    val quote: String?,

    @field:Size(max = 50)
    val author: String?,

    @field:Size(max = 2048)
    val description: String?,

    @field:NotEmpty
    @JoinColumn(name = "match_id", nullable = false)
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    val matchPlayers: Set<MatchPlayer>
)
