package com.harlyn.service;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by wannabe on 20.11.15.
 */
@Service
public class ProblemService {
    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private SolutionRepository solutionRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Resource
    private Map<Problem.ProblemType, ProblemHandler> problemHandlers;

    public Problem getById(Long id) {
        return problemRepository.findOne(id);
    }

    public Long createSolution(Problem problem, SubmitData data, User solver) {
        if (problem.getSolverTeams().contains(solver.getTeam())) {
            throw new TeamAlreadySolveProblemException("Team already solved this problem");
        }

        ProblemHandler problemHandler = problemHandlers.get(problem.getProblemType());
        Solution solution = new Solution(problem, solver)
                .setAnswer(data.getQueryParam());
        boolean success = problemHandler.checkSolution(problem, data, solver);
        if (!problemHandler.isManual()) {
            if (success) {
                solveProblem(problem, solution);
            } else {
                failSolution(solution);
            }
        } else {
            solutionRepository.saveAndFlush(solution);
        }
        return solution.getId();

    }

    @Transactional
    public void failSolution(Solution solution) {
        solution.setChecked(true);
        solution.setCorrect(false);
        solutionRepository.saveAndFlush(solution);
    }

    @Transactional
    public void solveProblem(Problem problem, Solution solution) {
        solution.setChecked(true);
        solution.setCorrect(true);

        teamRepository.incrementTeamPoints(solution.getSolver().getTeam().getId(), problem.getPoints());
        solutionRepository.save(solution);

        problem.getSolverTeams().add(solution.getSolver().getTeam());
        problemRepository.save(problem);

        Team team = teamRepository.findOne(solution.getSolver().getTeam().getId());
        team.getSolvedProblems().add(problem);
        teamRepository.save(team);

        teamRepository.flush();
        solutionRepository.flush();
        problemRepository.flush();
    }

    public ProblemService setProblemRepository(ProblemRepository problemRepository) {
        this.problemRepository = problemRepository;
        return this;
    }

    public ProblemService setSolutionRepository(SolutionRepository solutionRepository) {
        this.solutionRepository = solutionRepository;
        return this;
    }

    public ProblemService setTeamRepository(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
        return this;
    }

    public ProblemService setProblemHandlers(Map<Problem.ProblemType, ProblemHandler> problemHandlers) {
        this.problemHandlers = problemHandlers;
        return this;
    }

    /**
     * @param problem
     * @return id of problem
     */
    public Long createProblem(Problem problem) {
        return problemRepository.saveAndFlush(problem).getId();
    }

    public Long updateProblem(Problem problem, Problem updateData) {
        problem.setName(updateData.getName())
                .setAnswer(updateData.getAnswer())
                .setInfo(updateData.getInfo())
                .setPoints(updateData.getPoints())
                .setProblemType(updateData.getProblemType());
        return problemRepository.saveAndFlush(problem).getId();
    }

    public List<Problem> getAllProblems() {
        return problemRepository.findAll();
    }

    public List<Problem> getAviableProblems(Date currentDate) {
        return problemRepository.findAllByCurrentDate(currentDate);
    }

    public boolean isProblemAvailable(final Problem problem, Date currentDate) {
        if (problem.getEndDate() == null && problem.getStartDate() == null) {
            return true;
        }
        if (problem.getEndDate() == null) {
            return problem.getStartDate().before(currentDate);
        }
        if (problem.getStartDate() == null) {
            return problem.getEndDate().after(currentDate);
        }
        return problem.getStartDate().before(currentDate) && problem.getEndDate().after(currentDate);
    }
}
