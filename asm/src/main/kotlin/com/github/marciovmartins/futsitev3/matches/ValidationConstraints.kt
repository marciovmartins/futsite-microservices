package com.github.marciovmartins.futsitev3.matches

import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@Constraint(validatedBy = [BothTeams.BothTeamsConstraintValidator::class])
annotation class BothTeams(
    val message: String = "must have at least one player for team A and one player for team B",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<Payload>> = []
) {
    class BothTeamsConstraintValidator : ConstraintValidator<BothTeams, Set<MatchPlayer>> {
        override fun isValid(value: Set<MatchPlayer>?, context: ConstraintValidatorContext?): Boolean {
            return value == null || value.isEmpty() || hasMatchPlayersFromBothTeams(value)
        }

        private fun hasMatchPlayersFromBothTeams(value: Set<MatchPlayer>) = value.map { it.team }.toSet().size > 1
    }
}
