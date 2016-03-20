package com.harlyn.exception;

/**
 * Created by wannabe on 19.11.15.
 */
public class MissingInviteException extends RuntimeException {

	public MissingInviteException() {
		super("Missing invite");
	}
}
