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
import javax.persistence.Version
import javax.validation.Valid
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.PastOrPresent
import javax.validation.constraints.PositiveOrZero

@Suppress("unused")
@SameAmateurSoccerGroupId
@Entity(name = "gameDays")
class GameDay(
    @Id
    @Suppress("unused")
    @Type(type = "uuid-char")
    @Column(length = 36, unique = true, nullable = false)
    var id: UUID? = null,

    @Type(type = "uuid-char")
    @Column(name = "amateur_soccer_group_id")
    var amateurSoccerGroupId: UUID,

    @field:PastOrPresent
    var date: LocalDate,

    @field:Valid
    @field:NotEmpty
    @field:UniqueMatchOrder
    @field:SequentialMatchOrder
    @JoinColumn(name = "game_day_id", nullable = false)
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    var matches: Set<Match>,

    @Version
    var version: Long? = null,

    @JsonIgnore
    @Type(type = "uuid-char")
    @Column(name = "amateur_soccer_group_id", insertable = false, updatable = false)
    var persistedAmateurSoccerGroupId: UUID? = null,
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
