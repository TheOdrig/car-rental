package com.akif.rental.internal.exception;

import com.akif.shared.exception.BaseException;
import org.springframework.http.HttpStatus;

public class LateReturnException extends BaseException {
    
    public LateReturnException(String message) {
        super("LATE_RETURN_ERROR", message, HttpStatus.BAD_REQUEST);
    }
    
    public LateReturnException(String message, Throwable cause) {
        super("LATE_RETURN_ERROR", message, HttpStatus.BAD_REQUEST, cause);
    }
}
