package com.harlyn.exception;

/**
 * Created by wannabe on 18.11.15.
 */
public class MissingUserExcpetion extends RuntimeException {

	public MissingUserExcpetion() {
		super("Missing user");
	}
}
