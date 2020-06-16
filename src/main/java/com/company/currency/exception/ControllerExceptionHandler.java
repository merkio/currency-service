package com.company.currency.exception;

import com.company.currency.dto.ErrorResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {IllegalArgumentsException.class})
    protected ResponseEntity<ErrorResponse> handleIllegalArgument(RuntimeException ex, WebRequest request) {
        return ResponseEntity.badRequest().body(ErrorResponse.builder()
                                                    .status(HttpStatus.BAD_REQUEST.value())
                                                    .title(ex.getMessage())
                                                    .description(Objects.nonNull(ex.getCause()) ?
                                                                     ex.getCause().toString() :
                                                                     StringUtils.EMPTY)
                                                    .build());
    }
}
