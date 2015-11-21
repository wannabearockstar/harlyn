package com.harlyn.domain.problems.handlers;

import com.harlyn.domain.User;
import com.harlyn.domain.problems.Problem;
import com.harlyn.domain.problems.SubmitData;

/**
 * Created by wannabe on 20.11.15.
 */
public class InfoWebProblemHandler implements ProblemHandler {
    @Override
    public boolean isManual() {
        return true;
    }

    @Override
    public boolean checkSolution(Problem problem, SubmitData solution, User solver) {
        //do nothing cause we just saving answer id database
        return false;
    }
}
