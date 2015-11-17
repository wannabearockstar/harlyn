package com.harlyn.exception;

import com.harlyn.domain.User;

/**
 * Created by wannabe on 16.11.15.
 */
public class NonUniqueUserDataException extends RuntimeException {
    private final User user;

    public NonUniqueUserDataException(String message, User user) {
        super(message);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
