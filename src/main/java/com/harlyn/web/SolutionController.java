package com.harlyn.web;

import com.harlyn.domain.problems.Problem;
import com.harlyn.domain.problems.Solution;
import com.harlyn.exception.SolutionNotFoundException;
import com.harlyn.service.SolutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by wannabe on 21.11.15.
 */
@Controller
@RequestMapping(value = "/solution")
public class SolutionController {

	@Autowired
	private SolutionService solutionService;
	@Resource
	private Map<Problem.ProblemType, Problem> problemHandlers;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String solutionPage(@PathVariable(value = "id") Long id, Model model) {
		Solution solution = solutionService.getById(id);
		if (solution == null) {
			throw new SolutionNotFoundException();
		}
		model.addAttribute("solution", solution);
		model.addAttribute("problemHandlers", problemHandlers);
		return "solution/show";
	}
}
