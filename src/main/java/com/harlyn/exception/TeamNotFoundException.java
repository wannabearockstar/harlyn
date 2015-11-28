package com.harlyn.exception;

/**
 * Created by wannabe on 19.11.15.
 */
public class TeamNotFoundException extends RuntimeException {
    public TeamNotFoundException(Long teamId) {
        super("Team with id " + teamId + " not found");
    }
}
