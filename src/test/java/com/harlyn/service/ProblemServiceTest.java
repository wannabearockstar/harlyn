package com.harlyn.service;

import com.harlyn.HarlynApplication;
import com.harlyn.domain.Team;
import com.harlyn.domain.User;
import com.harlyn.domain.competitions.Competition;
import com.harlyn.domain.competitions.RegisteredTeam;
import com.harlyn.domain.problems.Problem;
import com.harlyn.domain.problems.Solution;
import com.harlyn.domain.problems.SubmitData;
import com.harlyn.domain.problems.handlers.ProblemHandler;
import com.harlyn.exception.TeamAlreadySolveProblemException;
import com.harlyn.repository.*;
import org.flywaydb.core.Flyway;
import org.flywaydb.test.junit.FlywayTestExecutionListener;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Created by wannabe on 20.11.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HarlynApplication.class)
@TransactionConfiguration
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, FlywayTestExecutionListener.class})
@WebAppConfiguration
@ActiveProfiles({"test"})
public class ProblemServiceTest {

	@Autowired
	PlatformTransactionManager platformTransactionManager;
	TransactionTemplate transactionTemplate;
	@Autowired
	private Flyway flyway;

	@Autowired
	private ProblemRepository problemRepository;

	@Autowired
	private SolutionRepository solutionRepository;

	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CompetitionRepository competitionRepository;

	@Autowired
	private RegisteredTeamRepository registeredTeamRepository;

	@Resource
	private Map<Problem.ProblemType, ProblemHandler> problemHandlers;

	private ProblemService problemService;

	@Before
	public void setUp() throws Exception {
		flyway.clean();
		flyway.migrate();

		problemService = new ProblemService();
		problemService.setProblemHandlers(problemHandlers)
			.setProblemRepository(problemRepository)
			.setSolutionRepository(solutionRepository)
			.setTeamRepository(teamRepository)
			.setRegisteredTeamRepository(registeredTeamRepository)
			.setEventPublisher(mock(ApplicationEventPublisher.class));
		transactionTemplate = new TransactionTemplate(platformTransactionManager);
	}

	@After
	public void tearDown() throws Exception {
		flyway.clean();
	}

	@Test
	public void testGetById() throws Exception {
		Competition competition = competitionRepository.saveAndFlush(new Competition("competition1"));
		Problem problem = problemRepository.saveAndFlush(new Problem("name", "answer", 12, Problem.ProblemType.FLAG, competition));

		assertEquals("name", problemService.getById(problem.getId()).getName());
		assertEquals("answer", problemService.getById(problem.getId()).getAnswer());
		assertEquals(new Integer(12), problemService.getById(problem.getId()).getPoints());
		assertEquals(Problem.ProblemType.FLAG, problemService.getById(problem.getId()).getProblemType());
	}

	@Test
	public void testCreateSolution() throws Exception {
		Competition competition = competitionRepository.saveAndFlush(new Competition("competition1"));
		Problem problem = problemRepository.saveAndFlush(new Problem("name", "answer", 12, Problem.ProblemType.FLAG, competition));
		Team team = teamRepository.saveAndFlush(new Team("name"));
		User solver = userRepository.saveAndFlush(new User("user@email.com", "username", "password")
			.setTeam(team)
		);
		RegisteredTeam registeredTeam = registeredTeamRepository.saveAndFlush(new RegisteredTeam(competition, team));

		assertEquals(new Integer(0), registeredTeam.getPoints());

		SubmitData invalidData = new SubmitData("wrong", null);
		SubmitData validData = new SubmitData("answer", null);

		Solution invalidSolution = solutionRepository.findOne(
			problemService.createSolution(problem, invalidData, solver)
		);
		team = teamRepository.findOne(team.getId());
		assertFalse(problemRepository.findOne(problem.getId()).getSolverTeams().contains(team));


		assertTrue(invalidSolution.isChecked());
		assertFalse(invalidSolution.isCorrect());
		assertEquals(new Integer(0), registeredTeam.getPoints());

		final Team updatedTeam = teamRepository.findOne(team.getId());
		transactionTemplate.execute(status -> {
			Solution validSolution = null;
			try {
				validSolution = solutionRepository.findOne(
					problemService.createSolution(problem, validData, solver)
				);
			} catch (IOException e) {
				e.printStackTrace();
			}

			assertTrue(validSolution.isChecked());
			assertTrue(validSolution.isCorrect());
			assertTrue(problemRepository.findOne(problem.getId()).getSolverTeams().contains(
				teamRepository.findOne(updatedTeam.getId())
			));
			assertTrue(teamRepository.findOne(updatedTeam.getId()).getSolvedProblems().contains(
				problemRepository.findOne(problem.getId())
			));
			assertEquals(new Integer(12), registeredTeamRepository.findOne(registeredTeam.getId()).getPoints());
			return 1;
		});
	}

	@Test
	public void testCreateSolutionTwiceFromTheSameTeam() throws Exception {
		Competition competition = competitionRepository.saveAndFlush(new Competition("competition1"));
		Problem problem = problemRepository.saveAndFlush(new Problem("name", "answer", 12, Problem.ProblemType.FLAG, competition));
		Team team = teamRepository.saveAndFlush(new Team("name"));
		User solver = userRepository.saveAndFlush(new User("user@email.com", "username", "password")
			.setTeam(team)
		);
		User anotherSolver = userRepository.saveAndFlush(new User("us1er@email.com", "use1rname", "password")
			.setTeam(team)
		);
		RegisteredTeam registeredTeam = registeredTeamRepository.saveAndFlush(new RegisteredTeam(competition, team));
		SubmitData validData = new SubmitData("answer", null);
		final Team updatedTeam = teamRepository.findOne(team.getId());
		transactionTemplate.execute(status -> {
			Solution validSolution = null;
			try {
				validSolution = solutionRepository.findOne(
					problemService.createSolution(problem, validData, solver)
				);
			} catch (IOException e) {
				e.printStackTrace();
			}

			assertTrue(validSolution.isChecked());
			assertTrue(validSolution.isCorrect());
			assertTrue(problemRepository.findOne(problem.getId()).getSolverTeams().contains(
				teamRepository.findOne(updatedTeam.getId())
			));
			assertEquals(new Integer(12), registeredTeamRepository.findOne(registeredTeam.getId()).getPoints());
			return 1;
		});

		try {
			transactionTemplate.execute(status -> {
				try {
					problemService.createSolution(problem, validData, anotherSolver);
				} catch (IOException e) {
					e.printStackTrace();
				}
				fail("Users from the same team solved same problem");
				return 1;
			});
		} catch (TeamAlreadySolveProblemException e) {
			assertTrue(true);
		}
	}

	@Test
	public void testGetAviableProblems() throws Exception {
		DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
		Date currentDate = formatter.parseDateTime("12/10/2015 14:10:30").toDate();
		Competition competition = competitionRepository.saveAndFlush(new Competition("competition1"));

		Problem problem = problemRepository.saveAndFlush(
			new Problem("name1", "answer", 12, Problem.ProblemType.FLAG, competition)
				.setStartDate(formatter.parseDateTime("12/10/2015 14:00:30").toDate())
				.setEndDate(formatter.parseDateTime("12/10/2015 15:00:30").toDate())
		);

		Problem problem1 = problemRepository.saveAndFlush(
			new Problem("name2", "answer", 12, Problem.ProblemType.FLAG, competition)
				.setStartDate(formatter.parseDateTime("12/10/2015 14:00:30").toDate())
		);

		Problem earlyProblem = problemRepository.saveAndFlush(
			new Problem("name3", "answer", 12, Problem.ProblemType.FLAG, competition)
				.setStartDate(formatter.parseDateTime("12/10/2015 13:00:30").toDate())
				.setEndDate(formatter.parseDateTime("12/10/2015 14:00:30").toDate())
		);

		Problem earlyProblem1 = problemRepository.saveAndFlush(
			new Problem("name4", "answer", 12, Problem.ProblemType.FLAG, competition)
				.setEndDate(formatter.parseDateTime("12/10/2015 14:00:30").toDate())
		);

		Problem lateProblem = problemRepository.saveAndFlush(
			new Problem("name5", "answer", 12, Problem.ProblemType.FLAG, competition)
				.setStartDate(formatter.parseDateTime("12/10/2015 15:00:30").toDate())
				.setEndDate(formatter.parseDateTime("12/10/2015 16:00:30").toDate())
		);

		List<Problem> aviableProblems = problemService.getAviableProblems(currentDate);

		assertEquals(2, aviableProblems.size());
		assertEquals("name2", aviableProblems.get(0).getName());
		assertEquals("name1", aviableProblems.get(1).getName());
	}

	@Test
	public void testIsProblemAvailable() throws Exception {
		DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
		Date currentDate = formatter.parseDateTime("12/10/2015 14:10:30").toDate();
		Competition competition = competitionRepository.saveAndFlush(new Competition("competition1"));

		Problem problem = new Problem("name1", "answer", 12, Problem.ProblemType.FLAG, competition)
			.setStartDate(formatter.parseDateTime("12/10/2015 14:00:30").toDate())
			.setEndDate(formatter.parseDateTime("12/10/2015 15:00:30").toDate());

		Problem problem1 = new Problem("name2", "answer", 12, Problem.ProblemType.FLAG, competition)
			.setStartDate(formatter.parseDateTime("12/10/2015 14:00:30").toDate());

		Problem earlyProblem = new Problem("name3", "answer", 12, Problem.ProblemType.FLAG, competition)
			.setStartDate(formatter.parseDateTime("12/10/2015 13:00:30").toDate())
			.setEndDate(formatter.parseDateTime("12/10/2015 14:00:30").toDate());

		Problem earlyProblem1 = new Problem("name4", "answer", 12, Problem.ProblemType.FLAG, competition)
			.setEndDate(formatter.parseDateTime("12/10/2015 14:00:30").toDate());

		Problem lateProblem = new Problem("name5", "answer", 12, Problem.ProblemType.FLAG, competition)
			.setStartDate(formatter.parseDateTime("12/10/2015 15:00:30").toDate())
			.setEndDate(formatter.parseDateTime("12/10/2015 16:00:30").toDate());

		assertTrue(problemService.isProblemAvailable(problem, currentDate));
		assertTrue(problemService.isProblemAvailable(problem1, currentDate));
		assertFalse(problemService.isProblemAvailable(earlyProblem, currentDate));
		assertFalse(problemService.isProblemAvailable(earlyProblem1, currentDate));
		assertFalse(problemService.isProblemAvailable(lateProblem, currentDate));
	}

	@Test
	public void testIsTeamSolvedPreviousProblem() throws Exception {
		Competition competition = competitionRepository.saveAndFlush(new Competition("competition1"));
		Problem problem = problemRepository.saveAndFlush(new Problem("name", "answer", 12, Problem.ProblemType.FLAG, competition));
		Problem prevProblem = problemRepository.saveAndFlush(new Problem("name1", "answer", 12, Problem.ProblemType.FLAG, competition));
		Team team = teamRepository.saveAndFlush(new Team("name"));
		User solver = userRepository.saveAndFlush(new User("user@email.com", "username", "password")
			.setTeam(team)
		);

		assertTrue(problemService.isTeamSolvedPreviousProblem(problem, solver.getTeam()));

		problem = problemRepository.saveAndFlush(problem.setPrevProblem(prevProblem));

		assertFalse(problemService.isTeamSolvedPreviousProblem(problem, solver.getTeam()));

		team.getSolvedProblems().add(prevProblem);
		team = teamRepository.saveAndFlush(team);

		assertTrue(problemService.isTeamSolvedPreviousProblem(problem, team));
	}
}