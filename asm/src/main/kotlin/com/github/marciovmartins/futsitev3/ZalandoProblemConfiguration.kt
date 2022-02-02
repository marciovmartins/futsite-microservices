package com.github.marciovmartins.futsitev3

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.rest.core.RepositoryConstraintViolationException
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.NativeWebRequest
import org.zalando.problem.Problem
import org.zalando.problem.Status
import org.zalando.problem.jackson.ProblemModule
import org.zalando.problem.spring.web.advice.ProblemHandling
import org.zalando.problem.violations.ConstraintViolationProblemModule
import org.zalando.problem.violations.Violation
import kotlin.reflect.full.cast

@Configuration
@Suppress("SpringFacetCodeInspection")
class ZalandoProblemConfiguration {
    @Bean
    fun problemModuleBean(): Module = ProblemModule()

    @Bean
    fun constraintViolationProblemModuleBean(): Module = ConstraintViolationProblemModule()
}

@ControllerAdvice
class ZalandoProblemExceptionHandler : ProblemHandling {
    override fun isCausalChainsEnabled() = false

    override fun handleMessageNotReadableException(
        exception: HttpMessageNotReadableException,
        request: NativeWebRequest
    ): ResponseEntity<Problem> {
        val problemBuilder = Problem.builder()
            .withTitle("Constraint Violation")
            .withStatus(Status.BAD_REQUEST)
        when (val cause = exception.cause) {
            is MissingKotlinParameterException -> problemBuilder.with(
                "violations", setOf(
                    Violation(cause.path.mapFieldsPath(), "cannot be null")
                )
            )
            is InvalidFormatException -> problemBuilder.with(
                "violations", setOf(
                    Violation(
                        cause.path.mapFieldsPath(),
                        if (cause.cause == null) {
                            cause.message
                        } else {
                            cause.cause!!.message
                        }!!,
                    )
                )
            )
            is JsonMappingException -> problemBuilder.with(
                "violations", setOf(
                    Violation(cause.path.mapFieldsPath(), cause.cause!!.message!!)
                )
            )
            else -> problemBuilder.withDetail(exception.message!!)
        }
        return create(problemBuilder.build(), request)
    }

    @ExceptionHandler
    fun handleRepositoryConstraintViolationException(
        exception: RepositoryConstraintViolationException,
        request: NativeWebRequest
    ): ResponseEntity<Problem> {
        val violations = exception.errors.allErrors
            .map { FieldError::class.cast(it) }
            .map { Violation(it.field, it.defaultMessage!!) }
        val problem = Problem.builder()
            .withTitle("Constraint Violation")
            .withStatus(Status.BAD_REQUEST)
            .with("violations", violations)
            .build()
        return create(problem, request)
    }
}

private fun Collection<JsonMappingException.Reference>.mapFieldsPath() =
    this.joinToString(separator = "") { mapPath(it) }.substring(1)

private fun mapPath(it: JsonMappingException.Reference): String = when (it.from) {
    is Collection<*> -> "[]"
    else -> ".${it.fieldName}"
}