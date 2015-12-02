package com.harlyn.web.admin;

import com.harlyn.domain.competitions.Competition;
import com.harlyn.domain.problems.Problem;
import com.harlyn.domain.problems.handlers.ProblemHandler;
import com.harlyn.exception.ProblemNotFoundException;
import com.harlyn.service.CompetitionService;
import com.harlyn.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * Created by wannabe on 22.11.15.
 */
@Controller
@RequestMapping(value = "/admin/problem")
public class AdminProblemController {
    @Autowired
    private ProblemService problemService;
    @Resource
    private Map<Problem.ProblemType, ProblemHandler> problemHandlers;
    @Autowired
    private CompetitionService competitionService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String newProblemPage(Model model) {
        model.addAttribute("problem_handlers_keys", problemHandlers.keySet());
        model.addAttribute("competitions", competitionService.findAll());
        return "admin/problem/new";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String newProblemAction(
            @RequestParam(value = "name") String name,
            @RequestParam(value = "answer") String answer,
            @RequestParam(value = "points") Integer points,
            @RequestParam(value = "problem_type") Problem.ProblemType problemType,
            @RequestParam(value = "info", required = false, defaultValue = "") String info,
            @RequestParam(value = "start_date", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss") Optional<Date> startDate,
            @RequestParam(value = "end_date", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss") Optional<Date> endDate,
            @RequestParam(value = "competition") Long competitionId
    ) {
        Competition competition = competitionService.findById(competitionId);
        Problem problemData = new Problem(name, answer, points, problemType, competition);
        if (startDate.isPresent()) {
            problemData.setStartDate(startDate.get());
        }
        if (endDate.isPresent()) {
            problemData.setEndDate(endDate.get());
        }
        problemData.setInfo(info);
        return "redirect:/admin/problem/" + problemService.createProblem(problemData);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String editProblemPage(@PathVariable(value = "id") Long id, Model model) {
        Problem problem = problemService.getById(id);
        if (problem == null) {
            throw new ProblemNotFoundException();
        }
        model.addAttribute("problem_handlers_keys", problemHandlers.keySet());
        model.addAttribute("problem", problem);
        return "admin/problem/edit";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public String editProblemAction(
            @PathVariable(value = "id") Long id,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "info", required = false, defaultValue = "") String info,
            @RequestParam(value = "start_date", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss") Optional<Date> startDate,
            @RequestParam(value = "end_date", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss") Optional<Date> endDate
    ) {
        Problem problem = problemService.getById(id);
        if (problem == null) {
            throw new ProblemNotFoundException();
        }
        Problem problemData = new Problem(name, problem.getAnswer(), problem.getPoints(), problem.getProblemType(), problem.getCompetition());
        if (startDate.isPresent()) {
            problemData.setStartDate(startDate.get());
        }
        if (endDate.isPresent()) {
            problemData.setEndDate(endDate.get());
        }
        problemData.setInfo(info);
        return "redirect:/admin/problem/" + problemService.updateProblem(problem, problemData);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listProblemPage(Model model) {
        model.addAttribute("problems", problemService.getAllProblems());
        return "admin/problem/list";
    }
}
