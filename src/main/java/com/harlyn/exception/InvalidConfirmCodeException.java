package com.harlyn.exception;

/**
 * Created by wannabe on 16.11.15.
 */
public class InvalidConfirmCodeException extends RuntimeException {
    public InvalidConfirmCodeException() {
        super("Invalid confirm code");
    }
}
