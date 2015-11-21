package com.harlyn.domain.problems.handlers;

import com.harlyn.domain.User;
import com.harlyn.domain.problems.Problem;
import com.harlyn.domain.problems.SubmitData;

/**
 * Created by wannabe on 20.11.15.
 */
public interface ProblemHandler {
    boolean isManual();
    boolean checkSolution(Problem problem, SubmitData solution, User solver);
}
