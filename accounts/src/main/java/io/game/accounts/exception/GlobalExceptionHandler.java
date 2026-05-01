package io.game.accounts.exception;

import io.game.accounts.common.dto.ResponseError;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = CustomerAlreadyExistsException.class)
    ResponseEntity<ResponseError> handleCustomerAlreadyExistException(
            CustomerAlreadyExistsException ex, WebRequest webRequest) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseError.builder()
                        .errorCode(HttpStatus.BAD_REQUEST)
                        .errorMessage(ex.getMessage())
                        .errorTime(LocalDateTime.now())
                        .apiPath(webRequest.getDescription(false))
                        .build());
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    ResponseEntity<ResponseError> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest webRequest) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ResponseError.builder()
                        .errorCode(HttpStatus.NOT_FOUND)
                        .errorMessage(ex.getMessage())
                        .errorTime(LocalDateTime.now())
                        .apiPath(webRequest.getDescription(false))
                        .build());
    }
}
