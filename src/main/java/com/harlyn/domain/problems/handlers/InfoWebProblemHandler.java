package com.harlyn.domain.problems.handlers;

import com.harlyn.domain.User;
import com.harlyn.domain.problems.Problem;
import com.harlyn.domain.problems.SubmitData;

/**
 * Created by wannabe on 20.11.15.
 */

/**
 * InfoWeb implementation of {@link ProblemHandler}
 * Do nothing, letting overlying service just save solution query param to persistence layer.
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
