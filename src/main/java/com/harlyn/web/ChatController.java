package com.harlyn.web;

import com.harlyn.domain.ChatMessage;
import com.harlyn.domain.User;
import com.harlyn.domain.competitions.Competition;
import com.harlyn.exception.CompetitionNotFoundException;
import com.harlyn.exception.TeamNotRegisteredForCompetitionException;
import com.harlyn.service.CompetitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.Date;

/**
 * Created by wannabe on 14.12.15.
 */
@Controller
public class ChatController {
    @Autowired
    private SimpMessagingTemplate template;
    @Autowired
    private CompetitionService competitionService;

    @RequestMapping(value = "/chat/team", method = RequestMethod.GET)
    public String teamChatPage(Model model) {
        return "chat/team";
    }

    @RequestMapping(value = "/competition/{id}/chat", method = RequestMethod.GET)
    public String competitionChatPage(@PathVariable("id") Long competitionId, Model model) {
        Competition competition = competitionService.findById(competitionId);
        if (competition == null) {
            throw new CompetitionNotFoundException(competitionId);
        }
        User me = (User) model.asMap().get("me");
        if (!competitionService.isTeamRegistered(competition, me.getTeam())) {
            throw new TeamNotRegisteredForCompetitionException();
        }
        model.addAttribute("competition", competition);
        return "chat/competition";
    }

    @MessageMapping("/team.{team_id}")
    public void teamHandler(@Payload ChatMessage body, @DestinationVariable("team_id") Long teamId, Principal principal) throws Exception {
        User author = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        template.convertAndSend("/out/team." + teamId, new ChatMessage(body.getContent(), new Date(), author));
    }

    @MessageMapping("/competition.{competition_id}")
    public void adminHandler(@Payload ChatMessage body, @DestinationVariable("competition_id") Long competitionId, Principal principal) {
        User author = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        if (!author.hasRole("ROLE_ADMIN")) {
            throw new AccessDeniedException("Only admins can publish in competition channel");
        }
        if (competitionService.findById(competitionId) == null) {
            throw new CompetitionNotFoundException(competitionId);
        }
        template.convertAndSend("/out/competition." + competitionId, new ChatMessage(body.getContent(), new Date(), author));
    }
}
