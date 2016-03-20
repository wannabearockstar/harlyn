package com.harlyn.exception;

/**
 * Created by wannabe on 06.12.15.
 */
public class ProblemFileNoutFoundException extends RuntimeException {

	public ProblemFileNoutFoundException() {
		super("Problem file not found");
	}
}
