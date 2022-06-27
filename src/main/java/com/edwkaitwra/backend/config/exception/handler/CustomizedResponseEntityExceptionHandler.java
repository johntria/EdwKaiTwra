package com.edwkaitwra.backend.config.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;


@ControllerAdvice
@Slf4j
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler implements ErrorController {


    @ExceptionHandler(UserIsNotActivatedException.class)
    public final ResponseEntity<?> userIsNotActivated(Exception ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(LocalDateTime.now(), HttpStatus.FORBIDDEN, ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.FORBIDDEN);
    }


//    @ExceptionHandler(Exception.class)
//    @RequestMapping("/error")
//    public final ResponseEntity<?> handleAllExceptions(Exception ex, WebRequest request) {
//        String returnMessage = ex.getMessage();
//        if (returnMessage == null || returnMessage.trim().isEmpty()) {
//            returnMessage = "Oups something gone wrong :)";
//        }
//        ExceptionResponse exceptionResponse = new ExceptionResponse(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR, returnMessage);
//        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
//    }


}
