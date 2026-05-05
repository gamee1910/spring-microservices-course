package io.game.accounts.exception;

import io.game.accounts.common.dto.ResponseError;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

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

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        Map<String, String> validationErrors = new HashMap<>();
        List<ObjectError> validationErrorList = ex.getBindingResult().getAllErrors();

        validationErrorList.forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String validationMsg = error.getDefaultMessage();
            validationErrors.put(fieldName, validationMsg);
        });
        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseError> handleGlobalException(Exception exception, WebRequest webRequest) {
        ResponseError errorResponseDTO = ResponseError.builder()
                .errorCode(HttpStatus.INTERNAL_SERVER_ERROR)
                .errorMessage(exception.getMessage())
                .errorTime(LocalDateTime.now())
                .apiPath(webRequest.getDescription(false))
                .build();

        return new ResponseEntity<>(errorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
