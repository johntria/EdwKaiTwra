package com.edwkaitwra.backend.config.exception.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ExceptionResponse {
    private final LocalDateTime timestamp;
    private final HttpStatus httpStatus;
    private final String message;
}
