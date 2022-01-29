package com.github.marciovmartins.futsitev3

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.BeanDescription
import com.fasterxml.jackson.databind.DeserializationConfig
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier
import com.fasterxml.jackson.databind.deser.std.EnumDeserializer
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.module.SimpleModule
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
import javax.annotation.concurrent.Immutable
import kotlin.reflect.full.cast

@Configuration
@Suppress("SpringFacetCodeInspection")
class ZalandoProblemConfiguration {
    @Bean
    fun problemModuleBean(): Module = ProblemModule()

    @Bean
    fun constraintViolationProblemModuleBean(): Module = ConstraintViolationProblemModule()

    @Bean
    fun myBeanSerializerModifierBean(): Module = SimpleModule().setDeserializerModifier(MyBeanDeserializerModifier())
}

@ControllerAdvice
class ExceptionHandler : ProblemHandling {
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
                "violations", listOf(
                    Violation(cause.path.mapFieldsPath(), "cannot be null")
                )
            )
            is InvalidFormatException -> problemBuilder.with(
                "violations", listOf(
                    MyViolation(
                        message = cause.cause!!.message!!,
                        field = cause.path.mapFieldsPath(),
                        invalidValue = cause.value
                    )
                )
            )
            is JsonMappingException -> when (val innerCause = cause.cause!!) {
                is InvalidValueException -> problemBuilder.with(
                    "violations", listOf(
                        MyViolation(
                            message = innerCause.message!!,
                            field = cause.path.mapFieldsPath(),
                            invalidValue = innerCause.invalidValue
                        )
                    )
                )
                else -> problemBuilder.with(
                    "violations", listOf(
                        Violation(cause.path.mapFieldsPath(), innerCause.message!!)
                    )
                )
            }
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

class MyBeanDeserializerModifier : BeanDeserializerModifier() {
    override fun modifyEnumDeserializer(
        config: DeserializationConfig?,
        type: JavaType?,
        beanDesc: BeanDescription?,
        deserializer: JsonDeserializer<*>
    ): JsonDeserializer<*> = when (deserializer) {
        is EnumDeserializer -> super.modifyEnumDeserializer(config, type, beanDesc, MyEnumDeserializer(deserializer))
        else -> super.modifyEnumDeserializer(config, type, beanDesc, deserializer)
    }

    class MyEnumDeserializer(deserializer: EnumDeserializer) :
        EnumDeserializer(deserializer, false) {
        override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): Any = try {
            super.deserialize(p, ctxt)
        } catch (ex: InvalidFormatException) {
            throw IllegalEnumArgumentException(this._valueClass.enumConstants, ex.value, ex)
        }
    }

    class IllegalEnumArgumentException(enums: Array<*>, override val invalidValue: Any, ex: Throwable) :
        InvalidValueException,
        IllegalArgumentException(
            "must be one of the values accepted: [%s]".format(enums.joinToString(", "), ex)
        )
}

interface InvalidValueException {
    val invalidValue: Any
}

@Immutable
@Suppress("unused")
private class MyViolation(val message: String, val field: String, val invalidValue: Any)

private fun Collection<JsonMappingException.Reference>.mapFieldsPath() =
    this.joinToString(separator = ".") { mapPath(it) }

private fun mapPath(it: JsonMappingException.Reference): String = when (it.from) {
    is Collection<*> -> it.index.toString()
    else -> it.fieldName
}