package com.harlyn.exception;

/**
 * Created by wannabe on 26.11.15.
 */
public class SolutionAlreadyCheckedException extends RuntimeException {

	public SolutionAlreadyCheckedException() {
		super("Solution already checked");
	}
}
