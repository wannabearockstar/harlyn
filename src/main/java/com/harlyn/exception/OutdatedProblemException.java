package com.harlyn.exception;

import com.harlyn.domain.problems.Problem;

/**
 * Created by wannabe on 30.11.15.
 */
public class OutdatedProblemException extends RuntimeException {
    private final Problem problem;
    public OutdatedProblemException(Problem problem) {
        super("Outdated problem");
        this.problem = problem;
    }

    public Problem getProblem() {
        return problem;
    }
}
