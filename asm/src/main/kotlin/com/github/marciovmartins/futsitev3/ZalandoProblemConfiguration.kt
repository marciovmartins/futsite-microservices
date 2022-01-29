package com.github.marciovmartins.futsitev3

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration
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
import javax.annotation.concurrent.Immutable
import kotlin.reflect.full.cast

@Configuration
@Suppress("SpringFacetCodeInspection")
@EnableAutoConfiguration(exclude = [ErrorMvcAutoConfiguration::class])
class ZalandoProblemConfiguration {
    @Bean
    fun problemModuleBean(): Module = ProblemModule()

    @Bean
    fun constraintViolationProblemModuleBean(): Module = ConstraintViolationProblemModule()
}

@ControllerAdvice
class ExceptionHandler : ProblemHandling {
    override fun handleMessageNotReadableException(
        exception: HttpMessageNotReadableException,
        request: NativeWebRequest
    ): ResponseEntity<Problem> {
        val problemBuilder = Problem.builder()
            .withTitle("Constraint Violation")
            .withStatus(Status.BAD_REQUEST)
        when (val cause = exception.cause) {
            is InvalidFormatException -> problemBuilder.with(
                "violations", listOf(
                    Violation(
                        field = cause.path.mapFieldsPath(),
                        message = cause.cause!!.message!!,
                        invalidValue = cause.value
                    )
                )
            )
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
            .map { Violation(field = it.field, message = it.defaultMessage!!) }
        val problem = Problem.builder()
            .withTitle("Constraint Violation")
            .withStatus(Status.BAD_REQUEST)
            .with("violations", violations)
            .build()
        return create(problem, request)
    }
}

@Immutable
@Suppress("unused")
private class Violation(val field: String, val message: String, val invalidValue: Any? = null)

private fun Collection<JsonMappingException.Reference>.mapFieldsPath() =
    this.joinToString(separator = ".") { mapPath(it) }

private fun mapPath(it: JsonMappingException.Reference): String = when (it.from) {
    is Collection<*> -> it.index.toString()
    else -> it.fieldName
}