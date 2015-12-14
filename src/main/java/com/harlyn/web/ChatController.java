package com.harlyn.web;

import com.harlyn.domain.ChatMessage;
import com.harlyn.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @RequestMapping(value = "/chat/team", method = RequestMethod.GET)
    public String teamChatPage(Model model) {
        return "chat/team";
    }

    @MessageMapping("/team.{team_id}")
    public void teamHandler(@Payload ChatMessage body, @DestinationVariable("team_id") Long teamId, Principal principal) throws Exception {
        User author = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        template.convertAndSend("/out/team." + teamId, new ChatMessage(body.getContent(), new Date(), author));
    }
}
