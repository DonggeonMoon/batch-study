package com.batchstudy.projecttwo.exception;

import com.batchstudy.projecttwo.dto.ErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Slf4j
public class CommonExceptionHandler {
    @ExceptionHandler(NoHandlerFoundException.class)
    public ErrorDto handleError404(HttpServletRequest request, Exception e) {
        log.warn("Request: " + request.getRequestURL() + " raised " + e);
        return new ErrorDto("404 - not found or all necessary parameters given.");
    }
}