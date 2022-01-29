package com.github.marciovmartins.futsitev3

import com.fasterxml.jackson.databind.Module
import org.apiguardian.api.API
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.rest.core.RepositoryConstraintViolationException
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.NativeWebRequest
import org.zalando.problem.Problem
import org.zalando.problem.Status
import org.zalando.problem.jackson.ProblemModule
import org.zalando.problem.spring.web.advice.AdviceTrait
import org.zalando.problem.spring.web.advice.ProblemHandling
import org.zalando.problem.violations.ConstraintViolationProblemModule
import org.zalando.problem.violations.Violation
import kotlin.reflect.full.cast


@Configuration
@EnableAutoConfiguration(exclude = [EmbeddedMongoAutoConfiguration::class])
class ZalandoProblemConfiguration {
    @Bean
    fun problemModuleBean(): Module = ProblemModule()

    @Bean
    fun constraintViolationProblemModuleBean(): Module = ConstraintViolationProblemModule()
}

@ControllerAdvice
class ExceptionHandler : ProblemHandling,
    RepositoryConstraintViolationProblemHandling

interface RepositoryConstraintViolationProblemHandling : AdviceTrait {
    @API(status = API.Status.INTERNAL)
    @ExceptionHandler
    fun handleMessageNotReadableException(
        exception: RepositoryConstraintViolationException,
        request: NativeWebRequest
    ): ResponseEntity<Problem> {
        val violations = exception.errors.allErrors
            .map { FieldError::class.cast(it) }
            .map { Violation(it.field, it.defaultMessage) }
        val problem = Problem.builder()
            .withTitle("Constraint Violation")
            .withStatus(Status.BAD_REQUEST)
            .with("violations", violations)
            .build()
        return create(problem, request)
    }
}
