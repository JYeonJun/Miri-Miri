package com.miri.coremodule.handler;

import com.miri.coremodule.dto.ResponseDto;
import com.miri.coremodule.handler.ex.CustomApiException;
import com.miri.coremodule.handler.ex.CustomValidationException;
import com.miri.coremodule.handler.ex.EmailAlreadyExistsException;
import com.miri.coremodule.handler.ex.OrderNotAvailableException;
import com.miri.coremodule.handler.ex.StockNotFoundException;
import com.miri.coremodule.handler.ex.StockUnavailableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<?> validationApiException(CustomValidationException e) {

        log.error(e.getMessage());
        return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage(), e.getErrorMap()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            CustomApiException.class,
            StockNotFoundException.class
    })
    public ResponseEntity<?> handleCustomApiExceptions(RuntimeException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage(), null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            StockUnavailableException.class,
            OrderNotAvailableException.class,
            EmailAlreadyExistsException.class
    })
    public ResponseEntity<?> handleApiExceptions(RuntimeException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ResponseDto<>(1, e.getMessage(), null), HttpStatus.OK);
    }
}
