package com.harlyn.exception;

import com.harlyn.domain.problems.Problem;

/**
 * Created by wannabe on 08.12.15.
 */
public class TeamNotSolverPreviousProblemException extends RuntimeException {
    public TeamNotSolverPreviousProblemException(Problem problem) {
        super("Problem require to solve " + problem.getPrevProblem().getName() + " first!");
    }
}
