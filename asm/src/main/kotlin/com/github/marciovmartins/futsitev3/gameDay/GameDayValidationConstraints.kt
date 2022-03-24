package com.github.marciovmartins.futsitev3.gameDay

import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Constraint(validatedBy = [SameAmateurSoccerGroupId.SameAmateurSoccerGroupIdConstraintValidator::class])
annotation class SameAmateurSoccerGroupId(
    val message: String = "Cannot update amateur soccer group id",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<Payload>> = []
) {
    class SameAmateurSoccerGroupIdConstraintValidator : ConstraintValidator<SameAmateurSoccerGroupId, GameDay> {
        override fun isValid(value: GameDay, context: ConstraintValidatorContext?) =
            value.persistedAmateurSoccerGroupId == null || matchAmateurSoccerGroupId(value)

        private fun matchAmateurSoccerGroupId(value: GameDay) =
            value.persistedAmateurSoccerGroupId == value.amateurSoccerGroupId
    }
}

@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@Constraint(validatedBy = [UniqueMatchOrder.ValidMatchOrdersConstraintValidator::class])
annotation class UniqueMatchOrder(
    val message: String = "must have valid match with sequential order",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<Payload>> = []
) {
    class ValidMatchOrdersConstraintValidator : ConstraintValidator<UniqueMatchOrder, Set<Match>> {
        override fun isValid(value: Set<Match>?, context: ConstraintValidatorContext?) =
            value == null || value.isEmpty() || hasUniqueMatchOrder(value)

        private fun hasUniqueMatchOrder(value: Set<Match>) = value.map { it.order }.toSet().size == value.size
    }
}

@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@Constraint(validatedBy = [SequentialMatchOrder.SequentialMatchOrderConstraintValidator::class])
annotation class SequentialMatchOrder(
    val message: String = "must have match order sequentially",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<Payload>> = []
) {
    class SequentialMatchOrderConstraintValidator : ConstraintValidator<SequentialMatchOrder, Set<Match>> {
        override fun isValid(value: Set<Match>?, context: ConstraintValidatorContext?) =
            value == null || value.isEmpty() || hasSequentialMatchOrder(value)

        private fun hasSequentialMatchOrder(value: Set<Match>): Boolean {
            val matchOrdersGreaterThanZero = value.mapNotNull { it.order }.filter { it > 0 }.toSet()
            val expectedMatchOrderSequence = generateSequence(1.toShort(), Short::inc).take(value.size).toSet()
            val shouldIgnore = matchOrdersGreaterThanZero.size != expectedMatchOrderSequence.size
            return shouldIgnore || matchOrdersGreaterThanZero == expectedMatchOrderSequence
        }
    }
}

@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@Constraint(validatedBy = [BothTeams.BothTeamsConstraintValidator::class])
annotation class BothTeams(
    val message: String = "must have at least one player statistic for team A and one player statistic for team B",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<Payload>> = []
) {
    class BothTeamsConstraintValidator : ConstraintValidator<BothTeams, Set<PlayerStatistic>> {
        override fun isValid(value: Set<PlayerStatistic>?, context: ConstraintValidatorContext?) =
            value == null || value.isEmpty() || hasMatchPlayerStatisticsFromBothTeams(value)

        private fun hasMatchPlayerStatisticsFromBothTeams(value: Set<PlayerStatistic>) =
            value.map { it.team }.toSet().size > 1
    }
}

@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@Constraint(validatedBy = [UniquePlayers.UniquePlayersConstraintValidator::class])
annotation class UniquePlayers(
    val message: String = "cannot have duplicated player user id",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<Payload>> = []
) {
    class UniquePlayersConstraintValidator : ConstraintValidator<UniquePlayers, Set<PlayerStatistic>> {
        override fun isValid(value: Set<PlayerStatistic>?, context: ConstraintValidatorContext?) =
            value == null || value.isEmpty() || hasUniquePlayerStatistics(value)

        private fun hasUniquePlayerStatistics(value: Set<PlayerStatistic>) =
            value.map { it.playerId }.toSet().size == value.size
    }
}
