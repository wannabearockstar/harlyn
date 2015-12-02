package com.harlyn.exception;

/**
 * Created by wannabe on 02.12.15.
 */
public class UserNotCaptainException extends RuntimeException {
    public UserNotCaptainException() {
        super("User do not have rights for registration team");
    }
}
