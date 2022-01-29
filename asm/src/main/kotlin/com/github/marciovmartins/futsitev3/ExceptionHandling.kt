package com.github.marciovmartins.futsitev3

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.exc.ValueInstantiationException
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import javax.servlet.http.HttpServletRequest
import javax.validation.ConstraintViolationException

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
        is ValueInstantiationException -> {
            val field = cause.path.mapFieldsPath()
            listOf(ResponseError(message = cause.cause!!.message!!, field = field))
        }
        else -> listOf(ResponseError(message = ex.message!!))
    }

    data class ResponseError(
        val message: String,
        val field: String? = null,
        val invalidValue: Any? = null
    )
}

private fun Collection<JsonMappingException.Reference>.mapFieldsPath() =
    this.joinToString(separator = ".") { mapPath(it) }

private fun mapPath(it: JsonMappingException.Reference): String = when (it.from) {
    is Collection<*> -> it.index.toString()
    else -> it.fieldName
}