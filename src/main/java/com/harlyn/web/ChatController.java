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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
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
    public void teamHandler(@Payload ChatMessage body, @DestinationVariable("team_id") Long teamId, Principal principal) throws Exception {
        User author = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        TeamChatMessage teamChatMessage = new TeamChatMessage(body.getContent(), new Date(), author, author.getTeam());
        eventPublisher.publishEvent(new MessagePublishedEvent(this, teamChatMessage));
        teamChatService.create(teamChatMessage);
    }

    @MessageMapping("/competition.{competition_id}")
    public void adminHandler(@Payload ChatMessage body, @DestinationVariable("competition_id") Long competitionId, Principal principal) {
        User author = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        if (!author.hasRole("ROLE_ADMIN")) {
            throw new AccessDeniedException("Only admins can publish in competition channel");
        }
        Competition competition = competitionService.findById(competitionId);
        if (competition == null) {
            throw new CompetitionNotFoundException(competitionId);
        }
        CompetitionChatMessage chatMessage = new CompetitionChatMessage(body.getContent(), new Date(), author, competition);
        eventPublisher.publishEvent(new MessagePublishedEvent(this, chatMessage));
        competitionChatService.create(chatMessage);
    }
}
