package com.github.marciovmartins.futsitev3.asm.gameDay

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.annotations.Type
import java.time.LocalDate
import java.util.UUID
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
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

@SameAmateurSoccerGroupId
@Entity(name = "gameDays")
class GameDay(
    @Id
    @Type(type = "uuid-char")
    @Column(length = 36, unique = true, nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
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
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)
    var matches: Set<Match>,

    @Version
    @JsonIgnore
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
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)
    var playerStatistics: Set<PlayerStatistic>
)

@Suppress("unused")
@Entity(name = "player_statistics")
class PlayerStatistic(
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Enumerated(EnumType.STRING)
    var team: Team,

    @field:NotNull
    @Type(type = "uuid-char")
    var playerId: UUID,

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
