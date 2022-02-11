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
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import javax.validation.Valid
import javax.validation.constraints.Max
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.PastOrPresent
import javax.validation.constraints.PositiveOrZero
import javax.validation.constraints.Size
import kotlin.reflect.KClass

@Suppress("unused")
@Entity(name = "gameDays")
class GameDay(
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

    @field:Valid
    @field:NotEmpty
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

    @Column(name = "match_order")
    var order: Short? = null,

    @field:Valid
    @field:NotEmpty
    @field:BothTeams
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
        A, B
    }
}

@MustBeDocumented
@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@Constraint(validatedBy = [BothTeams.BothTeamsConstraintValidator::class])
annotation class BothTeams(
    val message: String = "must have at least one player for team A and one player for team B",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<Payload>> = []
) {
    class BothTeamsConstraintValidator : ConstraintValidator<BothTeams, Set<Player>> {
        override fun isValid(value: Set<Player>?, context: ConstraintValidatorContext?): Boolean =
            value == null || value.isEmpty() || hasMatchPlayersFromBothTeams(value)

        private fun hasMatchPlayersFromBothTeams(value: Set<Player>) = value.map { it.team }.toSet().size > 1
    }
}