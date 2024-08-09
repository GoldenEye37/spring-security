package com.codeBuffer.securitydemo.exception.ex;

import com.codeBuffer.securitydemo.model.GenericResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({NotFoundException.class, UsernameNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GenericResponse handleNotFound(Exception e){
        return new GenericResponse(false, e.getMessage());
    }

    @ExceptionHandler(ExpiredException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public GenericResponse handleExpiredException(ExpiredException e){
        return new GenericResponse(false, e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public GenericResponse handleGenericRuntimeException(RuntimeException e){
        return new GenericResponse(false, e.getMessage());
    }
}
