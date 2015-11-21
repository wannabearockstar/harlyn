package com.harlyn.service;

import com.harlyn.HarlynApplication;
import com.harlyn.domain.Team;
import com.harlyn.domain.User;
import com.harlyn.domain.problems.Problem;
import com.harlyn.domain.problems.Solution;
import com.harlyn.domain.problems.SubmitData;
import com.harlyn.domain.problems.handlers.ProblemHandler;
import com.harlyn.exception.TeamAlreadySolveProblemException;
import com.harlyn.repository.ProblemRepository;
import com.harlyn.repository.SolutionRepository;
import com.harlyn.repository.TeamRepository;
import com.harlyn.repository.UserRepository;
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
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.Map;

import static org.junit.Assert.*;

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
                .setTeamRepository(teamRepository);
        transactionTemplate = new TransactionTemplate(platformTransactionManager);
    }

    @After
    public void tearDown() throws Exception {
        flyway.clean();
    }

    @Test
    public void testGetById() throws Exception {
        Problem problem = problemRepository.saveAndFlush(new Problem("name", "answer", 12, Problem.ProblemType.FLAG));

        assertEquals("name", problemService.getById(problem.getId()).getName());
        assertEquals("answer", problemService.getById(problem.getId()).getAnswer());
        assertEquals(new Integer(12), problemService.getById(problem.getId()).getPoints());
        assertEquals(Problem.ProblemType.FLAG, problemService.getById(problem.getId()).getProblemType());
    }

    @Test
    public void testCreateSolution() throws Exception {
        Problem problem = problemRepository.saveAndFlush(new Problem("name", "answer", 12, Problem.ProblemType.FLAG));
        Team team = teamRepository.saveAndFlush(new Team("name"));
        User solver = userRepository.saveAndFlush(new User("user@email.com", "username", "password")
                .setTeam(team)
        );

        assertEquals(new Integer(0), team.getPoints());

        SubmitData invalidData = new SubmitData("wrong", null);
        SubmitData validData = new SubmitData("answer", null);

        Solution invalidSolution = solutionRepository.findOne(
                problemService.createSolution(problem, invalidData, solver)
        );
        team = teamRepository.findOne(team.getId());
        assertFalse(problemRepository.findOne(problem.getId()).getSolverTeams().contains(team));


        assertTrue(invalidSolution.isChecked());
        assertFalse(invalidSolution.isCorrect());
        assertEquals(new Integer(0), team.getPoints());

        final Team updatedTeam = teamRepository.findOne(team.getId());
        transactionTemplate.execute(status -> {
            Solution validSolution = solutionRepository.findOne(
                    problemService.createSolution(problem, validData, solver)
            );

            assertTrue(validSolution.isChecked());
            assertTrue(validSolution.isCorrect());
            assertTrue(problemRepository.findOne(problem.getId()).getSolverTeams().contains(
                    teamRepository.findOne(updatedTeam.getId())
            ));
            assertTrue(teamRepository.findOne(updatedTeam.getId()).getSolvedProblems().contains(
                    problemRepository.findOne(problem.getId())
            ));
            assertEquals(new Integer(12), teamRepository.findOne(updatedTeam.getId()).getPoints());
            return 1;
        });
    }

    @Test
    public void testCreateSolutionTwiceFromTheSameTeam() throws Exception {
        Problem problem = problemRepository.saveAndFlush(new Problem("name", "answer", 12, Problem.ProblemType.FLAG));
        Team team = teamRepository.saveAndFlush(new Team("name"));
        User solver = userRepository.saveAndFlush(new User("user@email.com", "username", "password")
                        .setTeam(team)
        );
        User anotherSolver = userRepository.saveAndFlush(new User("us1er@email.com", "use1rname", "password")
                        .setTeam(team)
        );
        SubmitData validData = new SubmitData("answer", null);
        final Team updatedTeam = teamRepository.findOne(team.getId());
        transactionTemplate.execute(status -> {
            Solution validSolution = solutionRepository.findOne(
                    problemService.createSolution(problem, validData, solver)
            );

            assertTrue(validSolution.isChecked());
            assertTrue(validSolution.isCorrect());
            assertTrue(problemRepository.findOne(problem.getId()).getSolverTeams().contains(
                    teamRepository.findOne(updatedTeam.getId())
            ));
            assertEquals(new Integer(12), teamRepository.findOne(updatedTeam.getId()).getPoints());
            return 1;
        });

        try {
            transactionTemplate.execute(status -> {
                problemService.createSolution(problem, validData, anotherSolver);
                fail("Users from the same team solved same problem");
                return 1;
            });
        } catch (TeamAlreadySolveProblemException e) {
            assertTrue(true);
        }
    }
}