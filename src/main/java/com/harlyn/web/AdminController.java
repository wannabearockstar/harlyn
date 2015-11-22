package com.harlyn.web;

import com.harlyn.domain.problems.Problem;
import com.harlyn.domain.problems.handlers.ProblemHandler;
import com.harlyn.exception.ProblemNotFoundException;
import com.harlyn.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by wannabe on 21.11.15.
 */
@Controller
@RequestMapping(value = "/admin")
public class AdminController {
    @Autowired
    private ProblemService problemService;
    @Resource
    private Map<Problem.ProblemType, ProblemHandler> problemHandlers;

    @RequestMapping(value = "/problem/", method = RequestMethod.GET)
    public String newProblemPage(Model model) {
        model.addAttribute("problem_handlers_keys", problemHandlers.keySet());
        return "admin/problem/new";
    }

    @RequestMapping(value = "/problem/", method = RequestMethod.POST)
    public String newProblemAction(
            @RequestParam(value = "name") String name,
            @RequestParam(value = "answer") String answer,
            @RequestParam(value = "points") Integer points,
            @RequestParam(value = "problem_type") Problem.ProblemType problemType,
            @RequestParam(value = "info", required = false, defaultValue = "") String info
    ) {
        return "redirect:/admin/problem/" + problemService.createProblem(
                new Problem(name, answer, points, problemType)
                        .setInfo(info)
        );
    }

    @RequestMapping(value = "/problem/{id}", method = RequestMethod.GET)
    public String editProblemPage(@PathVariable(value = "id") Long id, Model model) {
        Problem problem = problemService.getById(id);
        if (problem == null) {
            throw new ProblemNotFoundException();
        }
        model.addAttribute("problem_handlers_keys", problemHandlers.keySet());
        model.addAttribute("problem", problem);
        return "admin/problem/edit";
    }

    @RequestMapping(value = "/problem/{id}", method = RequestMethod.POST)
    public String editProblemAction(
            @PathVariable(value = "id") Long id,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "answer") String answer,
            @RequestParam(value = "points") Integer points,
            @RequestParam(value = "problem_type") Problem.ProblemType problemType,
            @RequestParam(value = "info", required = false, defaultValue = "") String info
    ) {
        Problem problem = problemService.getById(id);
        if (problem == null) {
            throw new ProblemNotFoundException();
        }
        return "redirect:/admin/problem/" + problemService.updateProblem(
                problem,
                new Problem(name, answer, points, problemType)
                        .setInfo(info)
        );
    }

    @RequestMapping(value = "/problem/list", method = RequestMethod.GET)
    public String listProblemPage(Model model) {
        model.addAttribute("problems", problemService.getAllProblems());
        return "admin/problem/list";
    }
}
