package com.harlyn.service;

import com.harlyn.HarlynApplication;
import com.harlyn.domain.Team;
import com.harlyn.domain.User;
import com.harlyn.domain.UserSolvedProblems;
import com.harlyn.domain.competitions.Competition;
import com.harlyn.domain.competitions.RegisteredTeam;
import com.harlyn.domain.problems.Problem;
import com.harlyn.domain.problems.Solution;
import com.harlyn.repository.*;
import org.flywaydb.core.Flyway;
import org.flywaydb.test.junit.FlywayTestExecutionListener;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.math.BigInteger;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by wannabe on 10.12.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HarlynApplication.class)
@TransactionConfiguration
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, FlywayTestExecutionListener.class})
@WebAppConfiguration
@ActiveProfiles({"test"})
public class SolutionServiceTest {

	@Autowired
	private Flyway flyway;
	@Autowired
	private SolutionRepository solutionRepository;
	@Autowired
	private ProblemRepository problemRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CompetitionRepository competitionRepository;
	@Autowired
	private TeamRepository teamRepository;
	@Autowired
	private RegisteredTeamRepository registeredTeamRepository;
	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager em;
	private SolutionService solutionService;

	@Before
	public void setUp() throws Exception {
		flyway.clean();
		flyway.migrate();
		solutionService = new SolutionService().setSolutionRepository(solutionRepository)
			.setEm(em);
	}

	@After
	public void tearDown() throws Exception {
		flyway.clean();
	}

	@Test
	public void testGetById() throws Exception {
		Competition competition = competitionRepository.saveAndFlush(new Competition("asd"));
		Problem problem = problemRepository.saveAndFlush(new Problem("name", "answer", 12, Problem.ProblemType.FLAG, competition));
		Team team = teamRepository.saveAndFlush(new Team("name"));
		User user = userRepository.saveAndFlush(new User("email@email.com", "usrnm", "pswd", team));
		Solution solution = solutionRepository.saveAndFlush(new Solution(problem, user));

		assertEquals(problem, solutionService.getById(solution.getId()).getProblem());
		assertEquals(user, solutionService.getById(solution.getId()).getSolver());
	}

	@Test
	public void testGetAllSolutions() throws Exception {
		Competition competition = competitionRepository.saveAndFlush(new Competition("asd"));
		Problem problem = problemRepository.saveAndFlush(new Problem("name", "answer", 12, Problem.ProblemType.FLAG, competition));
		Team team = teamRepository.saveAndFlush(new Team("name"));
		User user = userRepository.saveAndFlush(new User("email@email.com", "usrnm", "pswd", team));
		User another = userRepository.saveAndFlush(new User("another@email.com", "usrnm", "pswd", team));
		solutionRepository.saveAndFlush(new Solution(problem, user));
		solutionRepository.saveAndFlush(new Solution(problem, another));

		List<Solution> solutions = solutionService.getAllSolutions();

		assertEquals(2, solutions.size());
		assertEquals(another, solutions.get(0).getSolver());
		assertEquals(user, solutions.get(1).getSolver());
	}

	@Test
	public void testGetAllUncheckedSolutions() throws Exception {
		Competition competition = competitionRepository.saveAndFlush(new Competition("asd"));
		Problem problem = problemRepository.saveAndFlush(new Problem("name", "answer", 12, Problem.ProblemType.FLAG, competition));
		Team team = teamRepository.saveAndFlush(new Team("name"));
		User user = userRepository.saveAndFlush(new User("email@email.com", "usrnm", "pswd", team));
		User another = userRepository.saveAndFlush(new User("another@email.com", "usrnm", "pswd", team));
		solutionRepository.saveAndFlush(new Solution(problem, user));
		Solution willBeChecked = solutionRepository.saveAndFlush(new Solution(problem, another));

		List<Solution> solutions = solutionService.getAllUncheckedSolutions();

		assertEquals(2, solutions.size());
		assertEquals(another, solutions.get(0).getSolver());
		assertEquals(user, solutions.get(1).getSolver());

		solutionRepository.saveAndFlush(willBeChecked.setChecked(true));

		solutions = solutionService.getAllUncheckedSolutions();

		assertEquals(1, solutions.size());
		assertEquals(user, solutions.get(0).getSolver());
	}

	@Test
	public void testGetUserSolverProblemsByCompetition() throws Exception {
		Competition competition = competitionRepository.saveAndFlush(new Competition("asd"));
		Competition anotherCompetition = competitionRepository.saveAndFlush(new Competition("12asd"));
		Problem problem1 = problemRepository.saveAndFlush(new Problem("name", "answer", 12, Problem.ProblemType.FLAG, competition));
		Problem problem2 = problemRepository.saveAndFlush(new Problem("nam1e", "answer", 15, Problem.ProblemType.FLAG, competition));
		Problem anotherProblem = problemRepository.saveAndFlush(new Problem("nam1e", "answer", 15, Problem.ProblemType.FLAG, anotherCompetition));
		Team team = teamRepository.saveAndFlush(new Team("name"));
		User user = userRepository.saveAndFlush(new User("email@email.com", "usrnm", "pswd", team));
		User another = userRepository.saveAndFlush(new User("another@email.com", "usrnm", "pswd", team));

		Solution solutionUserProblem1Correct = solutionRepository.save(new Solution(problem1, user).setCorrect(true));
		Solution solutionUserProblem2Inorrect = solutionRepository.save(new Solution(problem2, user).setCorrect(false));
		Solution solutionAnotherProblem1Unchecked = solutionRepository.save(new Solution(problem1, another));
		Solution solutionAnotherProblem2Correct = solutionRepository.save(new Solution(problem2, another).setCorrect(true));
		Solution solutionToAnotherCompetition = solutionRepository.save(new Solution(anotherProblem, another).setCorrect(true));
		solutionRepository.flush();
		registeredTeamRepository.saveAndFlush(new RegisteredTeam(competition, team));

		List<UserSolvedProblems> userSolvedProblems = solutionService.getUserSolverProblemsByCompetition(competition.getId());

		assertEquals(2, userSolvedProblems.size());
		assertEquals(1, userSolvedProblems.get(0).getProblems().size());
		assertEquals(problem2, userSolvedProblems.get(0).getProblems().iterator().next());
		assertEquals(BigInteger.valueOf(15l), userSolvedProblems.get(0).getPoints());
		assertEquals(another, userSolvedProblems.get(0).getSolver());

		assertEquals(1, userSolvedProblems.get(1).getProblems().size());
		assertEquals(user, userSolvedProblems.get(1).getSolver());
		assertEquals(problem1, userSolvedProblems.get(1).getProblems().iterator().next());
		assertEquals(BigInteger.valueOf(12l), userSolvedProblems.get(1).getPoints());
	}
}