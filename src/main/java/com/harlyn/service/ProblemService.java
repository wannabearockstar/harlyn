package com.harlyn.service;

import com.harlyn.domain.User;
import com.harlyn.domain.problems.Problem;
import com.harlyn.domain.problems.Solution;
import com.harlyn.domain.problems.SubmitData;
import com.harlyn.domain.problems.handlers.ProblemHandler;
import com.harlyn.repository.ProblemRepository;
import com.harlyn.repository.SolutionRepository;
import com.harlyn.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
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
        ProblemHandler problemHandler = problemHandlers.get(problem.getProblemType());
        Solution solution = new Solution(problem, solver)
                .setAnswer(data.getQueryParam());
        boolean success = problemHandler.checkSolution(problem, data);
        if (!problemHandler.isManual()) {
            if (success) {
                solveProblem(problem, solution);
            } else {
                failSolution(solution);
            }
        }
        return solution.getId();

    }

    @Transactional
    private void failSolution(Solution solution) {
        solution.setChecked(true);
        solution.setCorrect(false);
        solutionRepository.saveAndFlush(solution);
    }

    @Transactional
    private void solveProblem(Problem problem, Solution solution) {
        solution.setChecked(true);
        solution.setCorrect(true);
        teamRepository.incrementTeamPoints(solution.getSolver().getTeam().getId(), problem.getPoints());
        solutionRepository.saveAndFlush(solution);
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
}
