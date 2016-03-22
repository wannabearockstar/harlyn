package com.harlyn.web;

import com.harlyn.domain.User;
import com.harlyn.domain.competitions.Competition;
import com.harlyn.exception.CompetitionNotFoundException;
import com.harlyn.exception.OutdatedCompetitionException;
import com.harlyn.exception.TeamNotRegisteredForCompetitionException;
import com.harlyn.service.CategoryService;
import com.harlyn.service.CompetitionService;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * Created by wannabe on 02.12.15.
 */
@Controller
@RequestMapping(value = "/competition")
public class CompetitionController {

	@Autowired
	private CompetitionService competitionService;
	@Autowired
	private CategoryService categoryService;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String competitionPage(@PathVariable(value = "id") Long id, Model model) {
		Competition competition = competitionService.findById(id);
		if (competition == null) {
			throw new CompetitionNotFoundException(id);
		}
		model.addAttribute("competition", competition);
		model.addAttribute("available", competitionService.isCompetitionAvailable(competition, new Date()));
		model.addAttribute("registered", competitionService.isTeamRegistered(competition, ((User) model.asMap().get("me")).getTeam()));

		return "competition/show";
	}

	@RequestMapping(value = "/{id}/register", method = RequestMethod.POST)
	public String registerTeamForCompetition(@PathVariable(value = "id") Long id, Model model) {
		Competition competition = competitionService.findById(id);
		if (competition == null) {
			throw new CompetitionNotFoundException(id);
		}
		if (!competitionService.isCompetitionAvailable(competition, new Date())) {
			throw new OutdatedCompetitionException();
		}
		competitionService.registerUserTeamToCompetition(competition, (User) model.asMap().get("me"));
		return "redirect:/competition/" + id;
	}

	@RequestMapping(value = "/{id}/problems", method = RequestMethod.GET)
	public String problemsPage(@PathVariable(value = "id") Long id, Model model,
														 @RequestParam(value = "category", required = false, defaultValue = "0") String categoryId
	) {
		Competition competition = competitionService.findById(id);
		if (competition == null) {
			throw new CompetitionNotFoundException(id);
		}
		if (!competitionService.isTeamRegistered(competition, ((User) model.asMap().get("me")).getTeam())) {
			throw new TeamNotRegisteredForCompetitionException();
		}
		//// TODO: 13.03.16 custom mapper
		Long categoryIdParsed;
		if (!NumberUtils.isNumber(categoryId)) {
			categoryIdParsed = 0L;
		} else {
			categoryIdParsed = NumberUtils.toLong(categoryId);
		}

		model.addAttribute("competition", competition);
		model.addAttribute("selected_category_id", categoryIdParsed);
		model.addAttribute("categories", categoryService.findAllByCompetition(competition));
		return "competition/problems";
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String allCompetitionsPage(Model model) {
		model.addAttribute("competitions", competitionService.findAllAvailableCompetitions(new Date()));
		return "competition/list";
	}
}
