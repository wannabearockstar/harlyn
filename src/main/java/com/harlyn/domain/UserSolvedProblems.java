package com.harlyn.domain;

import com.harlyn.domain.problems.Problem;

import java.math.BigInteger;
import java.util.Set;

/**
 * Created by wannabe on 09.12.15.
 */
public class UserSolvedProblems {

	private final User solver;
	private final Set<Problem> problems;
	private final BigInteger points;

	public UserSolvedProblems(User user, Set<Problem> problems, BigInteger points) {
		this.solver = user;
		this.problems = problems;
		this.points = points;
	}

	public User getSolver() {
		return solver;
	}

	public Set<Problem> getProblems() {
		return problems;
	}

	public BigInteger getPoints() {
		return points;
	}
}
