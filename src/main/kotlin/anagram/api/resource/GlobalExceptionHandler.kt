package anagram.api.resource

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import string.lazyStr
import javax.validation.ConstraintViolationException


@ControllerAdvice
@Component
class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handle(exception: MethodArgumentNotValidException): Map<String, *> {
        return error(exception.bindingResult.fieldErrors
                .map { """Field ${it.field} with value ${it.rejectedValue} ${it.defaultMessage}""" })
    }


    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handle(exception: ConstraintViolationException): Map<String, *> {
        return error(exception.constraintViolations
                .map{ """${it.invalidValue} ${it.message}""" })
    }

    private fun error(errors: Collection<String>): Map<String, *> {
        logger.info("Client errors: [{}]", lazyStr {
            errors.joinToString(prefix = "[", postfix = "]", separator = "], [") })
        return mapOf("error" to errors)
    }

    companion object {
        val logger: Logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)
    }
}