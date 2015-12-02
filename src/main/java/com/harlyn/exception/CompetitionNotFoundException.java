package com.harlyn.exception;

/**
 * Created by wannabe on 02.12.15.
 */
public class CompetitionNotFoundException extends RuntimeException {
    public CompetitionNotFoundException(Long teamId) {
        super("Competition with id " + teamId + " not found");
    }
}
