package com.harlyn.web.admin;

import com.harlyn.domain.problems.Solution;
import com.harlyn.event.UserChangedEvent;
import com.harlyn.exception.MissingSolutionException;
import com.harlyn.exception.ProblemFileNoutFoundException;
import com.harlyn.exception.SolutionAlreadyCheckedException;
import com.harlyn.exception.SolutionNotFoundException;
import com.harlyn.service.FileService;
import com.harlyn.service.ProblemService;
import com.harlyn.service.SolutionService;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;

/**
 * Created by wannabe on 22.11.15.
 */
@Controller
@RequestMapping(value = "/admin/solution")
public class AdminSolutionController {

	public static final Logger logger = LoggerFactory.getLogger(AdminSolutionController.class);
	@Autowired
	private SolutionService solutionService;
	@Autowired
	private ProblemService problemService;
	@Autowired
	private ApplicationEventPublisher eventPublisher;
	@Autowired
	private FileService fileService;

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

	@RequestMapping(value = "/{id}/file", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> solutionFileAction(@PathVariable(value = "id") Long id, HttpServletResponse response) {
		Solution solution = solutionService.getById(id);
		if (solution == null) {
			throw new MissingSolutionException();
		}
		if (solution.getFile() == null) {
			throw new ProblemFileNoutFoundException();
		}
		File problemFile = fileService.getFileForSolution(solution);
		try {
			HttpHeaders respHeaders = new HttpHeaders();
			respHeaders.setContentType(MediaType.valueOf(solution.getFile().getContentType()));
			respHeaders.setContentLength(solution.getFile().getContentLength());
			respHeaders.setContentDispositionFormData("attachment",
				solution.getFile().getName()
					+ "."
					+ FilenameUtils.getExtension(
					problemFile.getName()
				));

			InputStreamResource isr = new InputStreamResource(new FileInputStream(problemFile));
			return new ResponseEntity<>(isr, respHeaders, HttpStatus.OK);
		} catch (Exception e) {
			logger.warn("Cant get file for problem: {}", e.getMessage());
			throw new SolutionNotFoundException();
		}
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String listSolutionPage(Model model) {
		model.addAttribute("solutions", solutionService.getAllSolutions());
		return "admin/solution/list";
	}

	@RequestMapping(value = "/unchecked", method = RequestMethod.GET)
	public String listUncheckedSolutionPage(Model model) {
		model.addAttribute("solutions", solutionService.getAllUncheckedSolutions());
		return "admin/solution/list_unchecked";
	}
}
