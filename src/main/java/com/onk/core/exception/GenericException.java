package com.onk.core.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenericException extends RuntimeException {

    private HttpStatus httpStatus;
    private ErrorCode errorCode;
    private String errorMessage;
}
