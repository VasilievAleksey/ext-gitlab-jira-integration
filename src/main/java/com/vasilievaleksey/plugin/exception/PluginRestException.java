package com.vasilievaleksey.plugin.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PluginRestException extends RuntimeException {
    private String message;
    private int httpStatusCode;

    public PluginRestException() {
        this.httpStatusCode = HttpStatus.BAD_REQUEST.value();
    }

    public PluginRestException(HttpStatus httpStatus, String message) {
        this.httpStatusCode = httpStatus.value();
        this.message = message;
    }
}
