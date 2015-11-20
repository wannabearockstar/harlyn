package com.harlyn.domain.problems.handlers;

import com.harlyn.domain.problems.Problem;

/**
 * Created by wannabe on 20.11.15.
 */
public class InfoEmailProblemHandler implements ProblemHandler {
    @Override
    public boolean isManual() {
        return true;
    }

    @Override
    public boolean checkSolution(Problem problem, Object solution) {
        return false;
    }
}
