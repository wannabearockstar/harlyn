package com.harlyn.service;

import com.harlyn.domain.User;
import com.harlyn.domain.UserSolvedProblems;
import com.harlyn.domain.problems.Problem;
import com.harlyn.domain.problems.Solution;
import com.harlyn.repository.SolutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by wannabe on 21.11.15.
 */
@Service
public class SolutionService {

	@PersistenceContext
	private EntityManager em;
	@Autowired
	private SolutionRepository solutionRepository;

	public Solution getById(Long id) {
		return solutionRepository.getById(id);
	}

	public List<Solution> getAllSolutions() {
		return solutionRepository.findAllByOrderByIdDesc();
	}

	public List<Solution> getAllUncheckedSolutions() {
		return solutionRepository.findByCheckedFalseOrderByIdDesc();
	}

	public List<UserSolvedProblems> getUserSolverProblemsByCompetition(long competitionId) {
		Query query = em.createNativeQuery("SELECT\n" +
			"  solver_id solver,\n" +
			"  string_agg(DISTINCT CAST(problem_id as TEXT), ',') problems,\n" +
			"  SUM(problems.points) points\n" +
			"FROM solutions\n" +
			"  LEFT OUTER JOIN problems\n" +
			"    ON problems.id = solutions.problem_id\n" +
			"  LEFT OUTER JOIN users\n" +
			"    ON solutions.solver_id = users.id\n" +
			"  INNER JOIN (\n" +
			"               SELECT DISTINCT ON (users.id) users.id\n" +
			"               FROM users\n" +
			"                 LEFT OUTER JOIN (\n" +
			"                                   SELECT *\n" +
			"                                   FROM registered_teams\n" +
			"                                 ) reg_teams ON reg_teams.team_id = users.team_id\n" +
			"\n" +
			"               WHERE reg_teams.competition_id = :competition_id_param\n" +
			"             ) registered_users\n" +
			"    ON registered_users.id = users.id\n" +
			"WHERE correct = TRUE AND problems.competition_id = :competition_id_param\n" +
			"GROUP BY solutions.solver_id\n" +
			"ORDER BY points DESC");
		query.setParameter("competition_id_param", competitionId);

		@SuppressWarnings("unchecked")
		List<Object> resultObjects = query.getResultList();
		return resultObjects.stream()
			.map(resultObj -> {
				Object[] res = (Object[]) resultObj;
				User user = em.getReference(User.class, Long.valueOf((Integer) res[0]));
				Set<Problem> problems = new HashSet<>(
					Arrays.asList(((String) res[1]).split(",")).stream()
						.map(s -> em.getReference(Problem.class, Long.parseLong(s)))
						.collect(Collectors.toSet())
				);
				BigInteger points = (BigInteger) res[2];
				return new UserSolvedProblems(user, problems, points);
			})
			.collect(Collectors.toList());
	}

	public SolutionService setEm(EntityManager em) {
		this.em = em;
		return this;
	}

	public SolutionService setSolutionRepository(SolutionRepository solutionRepository) {
		this.solutionRepository = solutionRepository;
		return this;
	}
}
