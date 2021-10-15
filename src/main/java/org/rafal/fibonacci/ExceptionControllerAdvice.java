package org.rafal.fibonacci;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
class ExceptionControllerAdvice {

    @ExceptionHandler(WrongFibonacciArgument.class)
    ResponseEntity<String> handleWrongArgument(WrongFibonacciArgument e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
