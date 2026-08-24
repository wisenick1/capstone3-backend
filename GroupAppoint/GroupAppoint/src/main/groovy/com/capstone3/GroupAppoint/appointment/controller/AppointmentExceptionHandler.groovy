package com.capstone3.GroupAppoint.appointment.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@RestControllerAdvice(assignableTypes = AppointmentController)
class AppointmentExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException)
    ResponseEntity<Map<String, Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = "Invalid value '${ex.value}' for parameter '${ex.name}'"
        Map<String, Object> body = [
                status : HttpStatus.BAD_REQUEST.value(),
                error  : HttpStatus.BAD_REQUEST.reasonPhrase,
                message: message
        ]
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body)
    }
}
