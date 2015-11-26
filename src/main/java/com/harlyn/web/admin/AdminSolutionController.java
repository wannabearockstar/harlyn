package com.harlyn.web.admin;

import com.harlyn.domain.problems.Solution;
import com.harlyn.exception.MissingSolutionException;
import com.harlyn.exception.SolutionAlreadyCheckedException;
import com.harlyn.service.ProblemService;
import com.harlyn.service.SolutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by wannabe on 22.11.15.
 */
@Controller
@RequestMapping(value = "/admin/solution")
public class AdminSolutionController {
    @Autowired
    private SolutionService solutionService;
    @Autowired
    private ProblemService problemService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String solutionPage(@PathVariable(value = "id") Long id, Model model) {
        Solution solution = solutionService.getById(id);
        if (solution == null) {
            throw new MissingSolutionException();
        }
        model.addAttribute("solution", solution);
        return "admin/solution/show";
    }

    @RequestMapping(value = "/{id}/check", method = RequestMethod.POST)
    public String checkSolution(@PathVariable(value = "id") Long id,
                                @RequestParam(value = "correct") boolean correct,
                                Model model
    ) {
        Solution solution = solutionService.getById(id);
        if (solution == null) {
            throw new MissingSolutionException();
        }
        if (solution.isChecked()) {
            throw new SolutionAlreadyCheckedException();
        }
        if (correct) {
            problemService.solveProblem(solution.getProblem(), solution);
        } else {
            problemService.failSolution(solution);
        }
        return "redirect:/admin/solution/" + id;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listSolutionPage(Model model) {
        model.addAttribute("solutions", solutionService.getAllSolutions());
        return "admin/solution/list";
    }

    @ExceptionHandler(MissingSolutionException.class)
    public ModelAndView missingSolution(MissingSolutionException e) {
        ModelAndView mav = new ModelAndView();
        mav.getModelMap().addAttribute("message", "Missing solution");
        mav.setViewName("utils/error/default");
        return mav;
    }

    @ExceptionHandler(SolutionAlreadyCheckedException.class)
    public ModelAndView solutionAlreadyCheckedException(SolutionAlreadyCheckedException e) {
        ModelAndView mav = new ModelAndView();
        mav.getModelMap().addAttribute("message", "Solution already checked");
        mav.setViewName("utils/error/default");
        return mav;
    }
}
