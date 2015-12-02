package com.harlyn.web;

import com.harlyn.domain.User;
import com.harlyn.domain.problems.Problem;
import com.harlyn.domain.problems.SubmitData;
import com.harlyn.event.UserChangedEvent;
import com.harlyn.exception.OutdatedCompetitionException;
import com.harlyn.exception.OutdatedProblemException;
import com.harlyn.exception.ProblemNotFoundException;
import com.harlyn.service.CompetitionService;
import com.harlyn.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * Created by wannabe on 20.11.15.
 */
@Controller
@RequestMapping("/problem")
public class ProblemController {
    @Autowired
    private ProblemService problemService;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    private CompetitionService competitionService;

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

        Long solutionId = problemService.createSolution(problem, new SubmitData(queryParam, fileParam), (User) model.asMap().get("me"));
        eventPublisher.publishEvent(new UserChangedEvent(this, (User) model.asMap().get("me")));
        return "redirect:/solution/" + solutionId;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String problemPage(@PathVariable(value = "id") Long id, Model model) {
        Problem problem = problemService.getById(id);
        if (problem == null) {
            throw new ProblemNotFoundException();
        }
        Date currentDate = new Date();
        model.addAttribute("problem", problem);
        model.addAttribute("available", competitionService.isCompetitionAvailable(problem.getCompetition(), currentDate)
                        || problemService.isProblemAvailable(problem, currentDate)
        );

        return "problem/show";
    }
}
