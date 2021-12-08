package com.github.marciovmartins.futsitev3

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
    ) = ex.constraintViolations.map {
        ResponseError(
            message = it.message,
            field = it.propertyPath.toString(),
            invalidValue = it.invalidValue,
        )
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleMessageNotReadableException(
        req: HttpServletRequest,
        ex: HttpMessageNotReadableException
    ): List<ResponseError> {
        val cause = ex.cause
        if (cause is MissingKotlinParameterException) {
            return listOf(ResponseError(
                message = "cannot be null",
                field = cause.path.joinToString(separator = ".") { it.fieldName }
            ))
        }
        return listOf(ResponseError(message = ex.message!!))
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RepositoryConstraintViolationException::class)
    fun handleRepositoryConstraintViolationException(
        req: HttpServletRequest,
        ex: RepositoryConstraintViolationException
    ) = ex.errors.allErrors
        .map { FieldError::class.cast(it) }
        .map {
            ResponseError(
                message = it.defaultMessage!!,
                field = it.field,
                invalidValue = it.rejectedValue
            )
        }

    data class ResponseError(
        val message: String,
        val field: String? = null,
        val invalidValue: Any? = null
    )
}