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
import com.fasterxml.jackson.databind.exc.ValueInstantiationException
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
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

@Configuration
class ExceptionHandlingConfiguration {
    @Bean
    fun myBeanSerializerModifierBean(): Module = SimpleModule().setDeserializerModifier(MyBeanDeserializerModifier())
}

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
        is JsonMappingException -> when (val innerCause = cause.cause!!) {
            is InvalidValueException -> {
                val field = cause.path.mapFieldsPath()
                val invalidValue = innerCause.invalidValue
                listOf(ResponseError(message = innerCause.message!!, field = field, invalidValue = invalidValue))
            }
            else -> {
                val field = cause.path.mapFieldsPath()
                listOf(ResponseError(message = innerCause.message!!, field = field))
            }
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

private fun Collection<JsonMappingException.Reference>.mapFieldsPath() =
    this.joinToString(separator = ".") { mapPath(it) }

private fun Throwable.invalidValue() = when (val cause = this.cause) {
    is InvalidValueException -> cause.invalidValue
    else -> null
}

private fun mapPath(it: JsonMappingException.Reference): String = when (it.from) {
    is Collection<*> -> it.index.toString()
    else -> it.fieldName
}