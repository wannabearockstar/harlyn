package com.harlyn.web;

import com.harlyn.domain.User;
import com.harlyn.domain.problems.Problem;
import com.harlyn.domain.problems.SubmitData;
import com.harlyn.exception.ProblemNotFoundException;
import com.harlyn.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by wannabe on 20.11.15.
 */
@Controller
@RequestMapping("/problem")
public class ProblemController {
    @Autowired
    private ProblemService problemService;

    @Transactional
    @RequestMapping(value = "/{id}/submit", method = RequestMethod.POST)
    public String submitSolution(@PathVariable(value = "id") Long id,
                                 @RequestParam(value = "query", required = false) String queryParam,
                                 @RequestParam(value = "file", required = false) MultipartFile fileParam
    ) {
        Problem problem = problemService.getById(id);
        if (problem == null) {
            throw new ProblemNotFoundException();
        }
        User solver = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long solutionId = problemService.createSolution(problem, new SubmitData(queryParam, fileParam), solver);
        return "redirect:/solution/" + solutionId;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String problemPage(@PathVariable(value = "id") Long id, Model model) {
        Problem problem = problemService.getById(id);
        if (problem == null) {
            throw new ProblemNotFoundException();
        }
        User me = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        model.addAttribute("problem", problem);
        model.addAttribute("me", me);

        return "problem/show";
    }
}
