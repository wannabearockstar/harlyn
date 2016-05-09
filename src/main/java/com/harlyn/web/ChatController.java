package com.harlyn.web;

import com.harlyn.domain.User;
import com.harlyn.domain.chat.ChatMessage;
import com.harlyn.domain.chat.CompetitionChatMessage;
import com.harlyn.domain.chat.TeamChatMessage;
import com.harlyn.domain.competitions.Competition;
import com.harlyn.event.MessagePublishedEvent;
import com.harlyn.exception.CompetitionNotFoundException;
import com.harlyn.exception.TeamNotRegisteredForCompetitionException;
import com.harlyn.service.CompetitionChatService;
import com.harlyn.service.CompetitionService;
import com.harlyn.service.TeamChatService;
import com.harlyn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;

/**
 * Created by wannabe on 14.12.15.
 */
@Controller
public class ChatController {

	@Autowired
	private CompetitionService competitionService;
	@Autowired
	private CompetitionChatService competitionChatService;
	@Autowired
	private TeamChatService teamChatService;
	@Autowired
	private ApplicationEventPublisher eventPublisher;
	@Autowired
	private UserService userService;

	@RequestMapping(value = "/chat/team", method = RequestMethod.GET)
	public String teamChatPage(Model model,
														 @RequestParam(value = "num", required = false, defaultValue = "10") int num) {

		User me = (User) model.asMap().get("me");
		model.addAttribute("messages", teamChatService.getLastMessagesByTeam(me.getTeam(), num));
		return "chat/team";
	}

	@RequestMapping(value = "/competition/{id}/chat", method = RequestMethod.GET)
	public String competitionChatPage(@PathVariable("id") Long competitionId,
																		@RequestParam(value = "num", required = false, defaultValue = "10") int num,
																		Model model
	) {
		Competition competition = competitionService.findById(competitionId);
		if (competition == null) {
			throw new CompetitionNotFoundException(competitionId);
		}
		User me = (User) model.asMap().get("me");
		if (!competitionService.isTeamRegistered(competition, me.getTeam())) {
			throw new TeamNotRegisteredForCompetitionException();
		}
		model.addAttribute("competition", competition);
		model.addAttribute("messages", competitionChatService.getLastMessagesByCompetition(competition, num));
		return "chat/competition";
	}

	@MessageMapping("/team.{team_id}")
	public void teamHandler(@Payload ChatMessage body, @DestinationVariable("team_id") Long teamId, Principal principal)
		throws Exception {
		User author = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
		TeamChatMessage teamChatMessage = new TeamChatMessage(body.getContent(), new Date(), author, author.getTeam());
		eventPublisher.publishEvent(new MessagePublishedEvent(this, teamChatService.create(teamChatMessage)));
	}

	@MessageMapping("/competition.{competition_id}")
	public void adminHandler(@Payload ChatMessage body, @DestinationVariable("competition_id") Long competitionId, Principal principal) {
		User author = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
		//reload for visibility of banned field
		// FIXME: 24.03.16
		author = userService.getById(author.getId());
		if (author.isBannedInChat()) {
			return;
		}
		Competition competition = competitionService.findById(competitionId);
		if (competition == null) {
			throw new CompetitionNotFoundException(competitionId);
		}
		// FIXME: 09.05.16 hotfixes
		body.setContent(body.getContent().trim());
		body.setContent(body.getContent().substring(0, Math.min(body.getContent().length(), 140)));
		if (body.getContent().isEmpty()) {
			return;
		}
		ChatMessage lastMessageByAuthor = competitionChatService.getLastMessageByAuthor(author);
		if (!author.isAdmin() && lastMessageByAuthor != null && ChronoUnit.MINUTES.between(
			lastMessageByAuthor.getPostedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
			LocalDateTime.now()) < 1L
			) {
			return;
		}
		CompetitionChatMessage chatMessage = new CompetitionChatMessage(body.getContent(), new Date(), author, competition);
		eventPublisher.publishEvent(new MessagePublishedEvent(this, competitionChatService.create(chatMessage)));
	}

	@RequestMapping(value = "/competition/{id}/chat/last", method = RequestMethod.GET)
	public ResponseEntity<Collection<? extends ChatMessage>> getLastMessages(
		@PathVariable("id") Long competitionId,
		@RequestParam(value = "num", required = false, defaultValue = "200") int num
	) {
		Competition competition = competitionService.findById(competitionId);
		if (competition == null) {
			throw new CompetitionNotFoundException(competitionId);
		}
		return ResponseEntity.ok(competitionChatService.getLastMessagesByCompetition(competition, num));
	}
}
