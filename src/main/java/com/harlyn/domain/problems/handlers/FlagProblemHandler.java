package com.harlyn.domain.problems.handlers;

import com.harlyn.domain.User;
import com.harlyn.domain.problems.Problem;
import com.harlyn.domain.problems.SubmitData;

import java.util.Objects;

/**
 * Created by wannabe on 20.11.15.
 */

/**
 * Flag-based implementation of {@link ProblemHandler}.
 * Compare solution query param to problem answer
 */
public class FlagProblemHandler implements ProblemHandler {

	@Override
	public boolean isManual() {
		return false;
	}

	@Override
	public boolean checkSolution(Problem problem, SubmitData solution, User solver) {
		return Objects.equals(problem.getAnswer(), solution.getQueryParam());
	}
}
