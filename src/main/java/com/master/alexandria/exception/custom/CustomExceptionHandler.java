package com.master.alexandria.exception.custom;

import com.master.alexandria.exception.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = UserNotFoundException.class)
    protected ResponseEntity<Object> handleUserNotFoundException(RuntimeException exception, WebRequest webRequest) {

        return  handleExceptionInternal(exception, new ErrorResponse(404, exception.getMessage()),
                new HttpHeaders(), HttpStatusCode.valueOf(404), webRequest);
    }
}
