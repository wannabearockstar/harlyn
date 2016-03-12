package com.harlyn.service;

import com.harlyn.domain.Team;
import com.harlyn.domain.User;
import com.harlyn.domain.competitions.RegisteredTeam;
import com.harlyn.domain.problems.*;
import com.harlyn.domain.problems.handlers.ProblemHandler;
import com.harlyn.exception.TeamAlreadySolveProblemException;
import com.harlyn.exception.TeamNotRegisteredForCompetitionException;
import com.harlyn.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
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
    @Autowired
    private RegisteredTeamRepository registeredTeamRepository;
    @Resource
    private Map<Problem.ProblemType, ProblemHandler> problemHandlers;
    @Autowired
    private FileService fileService;
    @Autowired
    private HintRepository hintRepository;

    public Problem getById(Long id) {
        return problemRepository.findOne(id);
    }

    public Long createSolution(Problem problem, SubmitData data, User solver) throws IOException {
        if (problem.getSolverTeams().contains(solver.getTeam())) {
            throw new TeamAlreadySolveProblemException("Team already solved this problem");
        }

        ProblemHandler problemHandler = problemHandlers.get(problem.getProblemType());
        Solution solution = new Solution(problem, solver)
                .setAnswer(data.getQueryParam());
        if (data.getFileParam() != null) {
            solution.setFile(fileService.uploadSolutionFile(
                    data.getFileParam(),
                    solution
            ));
        }
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
        RegisteredTeam registeredTeam = registeredTeamRepository.findOneByCompetitionAndTeam(problem.getCompetition(), solution.getSolver().getTeam());
        if (registeredTeam == null) {
            throw new TeamNotRegisteredForCompetitionException();
        }

        solution.setChecked(true);
        solution.setCorrect(true);

        registeredTeamRepository.incrementTeamPoints(registeredTeam.getId(), problem.getPoints());
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
                .setProblemType(updateData.getProblemType())
                .setStartDate(updateData.getStartDate())
                .setEndDate(updateData.getEndDate())
                .setCategory(updateData.getCategory())
                .setPrevProblem(updateData.getPrevProblem())
        ;
        if (updateData.getFile() != null) {
            ProblemFile problemFile = problem.getFile();
            if (problemFile != null) {
                problemFile.setName(updateData.getFile().getName());
                problemFile.setPath(updateData.getFile().getPath());
                problemFile.setContentLength(updateData.getFile().getContentLength());
                problemFile.setContentType(updateData.getFile().getContentType());
            } else {
                problem.setFile(updateData.getFile());
            }
        }
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

    public boolean isTeamSolvedPreviousProblem(Problem problem, Team team) {
        if (problem.getPrevProblem() == null) {
            return true;
        }
        return team.getSolvedProblems().contains(problem.getPrevProblem());
    }

    public ProblemService setRegisteredTeamRepository(RegisteredTeamRepository registeredTeamRepository) {
        this.registeredTeamRepository = registeredTeamRepository;
        return this;
    }

    public void addHint(Problem problem, String content) {
        Hint hint = new Hint(content, problem);
        hintRepository.saveAndFlush(hint);
    }

    public void deleteHint(Long id) {
        hintRepository.delete(id);
    }
}
