package com.harlyn.exception;

/**
 * Created by wannabe on 02.12.15.
 */
public class TeamAlreadyRegisteredException extends RuntimeException {
    public TeamAlreadyRegisteredException() {
        super("Team already register for this competition");
    }
}
