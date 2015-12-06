package com.harlyn.web;

import com.harlyn.domain.User;
import com.harlyn.domain.problems.Problem;
import com.harlyn.domain.problems.SubmitData;
import com.harlyn.event.UserChangedEvent;
import com.harlyn.exception.*;
import com.harlyn.service.CompetitionService;
import com.harlyn.service.FileService;
import com.harlyn.service.ProblemService;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Created by wannabe on 20.11.15.
 */
@Controller
@RequestMapping("/problem")
public class ProblemController {
    public static final Logger logger = LoggerFactory.getLogger(ProblemController.class);
    @Autowired
    private ProblemService problemService;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    private CompetitionService competitionService;
    @Autowired
    private FileService fileService;

    @Transactional
    @RequestMapping(value = "/{id}/submit", method = RequestMethod.POST)
    public String submitSolution(@PathVariable(value = "id") Long id,
                                 @RequestParam(value = "query", required = false) String queryParam,
                                 @RequestParam(value = "file", required = false) MultipartFile fileParam,
                                 Model model
    ) {
        Problem problem = problemService.getById(id);
        if (problem == null) {
            throw new ProblemNotFoundException();
        }
        Date currentDate = new Date();
        if (!problemService.isProblemAvailable(problem, currentDate)) {
            throw new OutdatedProblemException(problem);
        }
        if (!competitionService.isCompetitionAvailable(problem.getCompetition(), currentDate)) {
            throw new OutdatedCompetitionException();
        }

        try {
            Long solutionId = problemService.createSolution(problem, new SubmitData(queryParam, fileParam), (User) model.asMap().get("me"));
            eventPublisher.publishEvent(new UserChangedEvent(this, (User) model.asMap().get("me")));
            return "redirect:/solution/" + solutionId;
        } catch (IOException e) {
            logger.error("Error while uploading file: {}", e.getMessage());
            throw new InvalidFileException();
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String problemPage(@PathVariable(value = "id") Long id, Model model) {
        Problem problem = problemService.getById(id);
        if (problem == null) {
            throw new ProblemNotFoundException();
        }
        if (!competitionService.isTeamRegistered(problem.getCompetition(), ((User) model.asMap().get("me")).getTeam())) {
            throw new TeamNotRegisteredForCompetitionException();
        }
        Date currentDate = new Date();
        model.addAttribute("problem", problem);
        model.addAttribute("available", competitionService.isCompetitionAvailable(problem.getCompetition(), currentDate)
                        && problemService.isProblemAvailable(problem, currentDate)
        );

        return "problem/show";
    }

    @RequestMapping(value = "/{id}/file", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> problemFileAction(@PathVariable(value = "id") Long id, HttpServletResponse response) {
        Problem problem = problemService.getById(id);
        if (problem == null) {
            throw new ProblemNotFoundException();
        }
        if (problem.getFile() == null) {
            throw new ProblemFileNoutFoundException();
        }
        File problemFile = fileService.getFileForProblem(problem);
        try {
            HttpHeaders respHeaders = new HttpHeaders();
            respHeaders.setContentType(MediaType.valueOf(problem.getFile().getContentType()));
            respHeaders.setContentLength(problem.getFile().getContentLength());
            respHeaders.setContentDispositionFormData("attachment",
                    problem.getFile().getName()
                            + "."
                            + FilenameUtils.getExtension(
                            problemFile.getName()
                    ));

            InputStreamResource isr = new InputStreamResource(new FileInputStream(problemFile));
            return new ResponseEntity<>(isr, respHeaders, HttpStatus.OK);
        } catch (Exception e) {
            logger.warn("Cant get file for problem: {}", e.getMessage());
            throw new ProblemNotFoundException();
        }
    }
}
