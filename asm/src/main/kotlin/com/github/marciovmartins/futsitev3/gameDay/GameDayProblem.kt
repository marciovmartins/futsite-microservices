package com.github.marciovmartins.futsitev3.gameDay

import org.zalando.problem.AbstractThrowableProblem
import org.zalando.problem.Exceptional
import org.zalando.problem.Status
import org.zalando.problem.violations.Violation

class GameDayDateNotUniqueConstraintViolation : AbstractThrowableProblem(
    null,
    "Constraint Violation",
    Status.BAD_REQUEST,
    null
) {
    override fun getCause(): Exceptional? = super.cause

    @Suppress("unused")
    val violations = setOf(Violation("date", "A game day for this date already exists"))
}