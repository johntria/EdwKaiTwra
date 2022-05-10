package com.edwkaitwra.backend.config.exception.handler;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;


public record ExceptionResponse(LocalDateTime timestamp, HttpStatus httpStatus, String message) {
}
