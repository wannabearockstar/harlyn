package com.harlyn.web.admin;

import com.harlyn.domain.problems.Solution;
import com.harlyn.event.UserChangedEvent;
import com.harlyn.exception.MissingSolutionException;
import com.harlyn.exception.SolutionAlreadyCheckedException;
import com.harlyn.service.ProblemService;
import com.harlyn.service.SolutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
    @Autowired
    private ApplicationEventPublisher eventPublisher;

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
        eventPublisher.publishEvent(new UserChangedEvent(this, solution.getSolver()));
        return "redirect:/admin/solution/" + id;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String listSolutionPage(Model model) {
        model.addAttribute("solutions", solutionService.getAllSolutions());
        return "admin/solution/list";
    }
}
