package com.onk.core.exception;

import com.onk.core.results.ErrorResult;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err -> {
            errors.put(err.getField(), err.getDefaultMessage());
        });
        return ResponseEntity.badRequest()
                .body(errors);
    }

    @ExceptionHandler(GenericException.class)
    public ResponseEntity<?> handleException(GenericException exception){
        Map<String, Object> errors = new HashMap<>();
        errors.put("status", exception.getHttpStatus());
        errors.put("message", exception.getErrorCode());
        errors.put("detail", exception.getErrorMessage());
        return ResponseEntity.status(exception.getHttpStatus())
                .body(errors);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxUploadSizeException(){
        return new ResponseEntity<>(new ErrorResult("File size is long.."), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpClientErrorException.Forbidden.class)
    public ResponseEntity<?> forbiddenError(){
        return new ResponseEntity<>(new ErrorResult("Forbidden"), HttpStatus.FORBIDDEN);
    }
}
