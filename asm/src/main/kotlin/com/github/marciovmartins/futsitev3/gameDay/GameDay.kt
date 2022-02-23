package com.github.marciovmartins.futsitev3.gameDay

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.Type
import java.time.LocalDate
import java.util.UUID
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToMany
import javax.validation.Valid
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.PastOrPresent
import javax.validation.constraints.PositiveOrZero
import javax.validation.constraints.Size

@Suppress("unused")
@Entity(name = "gameDays")
class GameDay(
    @Id
    @Suppress("unused")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Type(type = "uuid-char")
    var amateurSoccerGroupId: UUID,

    @field:PastOrPresent
    var date: LocalDate,

    @field:Size(max = 255)
    var quote: String?,

    @field:Size(max = 50)
    var author: String?,

    @field:Size(max = 2048)
    var description: String?,

    @field:Valid
    @field:NotEmpty
    @field:UniqueMatchOrder
    @field:SequentialMatchOrder
    @JoinColumn(name = "game_day_id", nullable = false)
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    var matches: Set<Match>,
)

@Suppress("unused")
@Entity(name = "matches")
class Match(
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @field:Min(1)
    @Column(name = "match_order")
    var order: Short? = null,

    @field:Valid
    @field:NotEmpty
    @field:BothTeams
    @field:UniquePlayers
    @JoinColumn(name = "match_id", nullable = false)
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    var players: Set<Player>
)

@Suppress("unused")
@Entity(name = "players")
class Player(
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Enumerated(EnumType.STRING)
    var team: Team,

    @field:NotNull
    @Type(type = "uuid-char")
    var userId: UUID,

    @field:Max(99)
    @field:PositiveOrZero
    var goalsInFavor: Short,

    @field:Max(99)
    @field:PositiveOrZero
    var goalsAgainst: Short,

    @field:Max(99)
    @field:PositiveOrZero
    var yellowCards: Short,

    @field:Max(99)
    @field:PositiveOrZero
    var blueCards: Short,

    @field:Max(99)
    @field:PositiveOrZero
    var redCards: Short,
) {
    enum class Team {
        A, B
    }
}
