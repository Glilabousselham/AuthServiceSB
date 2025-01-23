package org.glila.auth.config.exception.handler;

import org.springframework.http.HttpStatus;

public class ErrorMessageException extends RuntimeException {

    private String message;
    private HttpStatus httpStatus;

    public ErrorMessageException(String message, HttpStatus httpStatus) {
        super(message);

        this.message = message;
        this.httpStatus = httpStatus;
    }


    public String getMessage() {
        return this.message;
    }

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }
}
