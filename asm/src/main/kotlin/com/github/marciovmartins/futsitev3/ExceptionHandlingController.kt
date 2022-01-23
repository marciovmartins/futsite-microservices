package com.github.marciovmartins.futsitev3

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.exc.ValueInstantiationException
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import org.springframework.data.rest.core.RepositoryConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import javax.servlet.http.HttpServletRequest
import javax.validation.ConstraintViolationException
import kotlin.reflect.full.cast

@ControllerAdvice
class ExceptionHandlingController {
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolation(
        req: HttpServletRequest,
        ex: ConstraintViolationException
    ): List<ResponseError> = ex.constraintViolations.map {
        ResponseError(message = it.message, field = it.propertyPath.toString(), invalidValue = it.invalidValue)
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleMessageNotReadableException(
        req: HttpServletRequest,
        ex: HttpMessageNotReadableException
    ): List<ResponseError> = when (val cause = ex.cause) {
        is MissingKotlinParameterException -> {
            val field = cause.path.mapFieldsPath()
            listOf(ResponseError(message = "cannot be null", field = field))
        }
        is ValueInstantiationException -> {
            val field = cause.path.mapFieldsPath()
            val invalidValue = cause.invalidValue()
            listOf(ResponseError(message = cause.cause!!.message!!, field = field, invalidValue = invalidValue))
        }
        is InvalidFormatException -> {
            val field = cause.path.mapFieldsPath()
            val invalidValue = cause.value
            listOf(ResponseError(message = cause.cause!!.message!!, field = field, invalidValue = invalidValue))
        }
        else -> listOf(ResponseError(message = ex.message!!))
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RepositoryConstraintViolationException::class)
    fun handleRepositoryConstraintViolationException(
        req: HttpServletRequest,
        ex: RepositoryConstraintViolationException
    ): List<ResponseError> = ex.errors.allErrors
        .map { FieldError::class.cast(it) }
        .map { ResponseError(message = it.defaultMessage!!, field = it.field, invalidValue = it.rejectedValue) }

    data class ResponseError(
        val message: String,
        val field: String? = null,
        val invalidValue: Any? = null
    )
}

private fun Collection<JsonMappingException.Reference>.mapFieldsPath() =
    this.joinToString(separator = ".") { mapPath(it) }

private fun ValueInstantiationException.invalidValue() = when (val cause = this.cause) {
    is IllegalEnumArgumentException -> cause.invalidValue
    else -> null
}

private fun mapPath(it: JsonMappingException.Reference): String = when (it.from) {
    is Collection<*> -> it.index.toString()
    else -> it.fieldName
}
