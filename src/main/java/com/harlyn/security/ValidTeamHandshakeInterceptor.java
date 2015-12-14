package com.harlyn.security;

import com.harlyn.domain.User;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.security.Principal;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Created by wannabe on 14.12.15.
 */
public class ValidTeamHandshakeInterceptor extends ChannelInterceptorAdapter {
    public static final Pattern teamPattern = Pattern.compile("/team\\.\\d+$");
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        if (headerAccessor.getCommand().equals(StompCommand.SUBSCRIBE) && teamPattern.matcher(headerAccessor.getDestination()).find()) {
            Principal user = headerAccessor.getUser();
            if (!validateTeamRights(user, headerAccessor.getDestination())) {
                throw new IllegalArgumentException("No permission for chat of this team");
            }
        }
        return message;
    }

    private boolean validateTeamRights(Principal user, String destination) {
        Long teamId = Long.parseLong(destination.replace("/out/team.", ""));
        User castedUser = (User) ((UsernamePasswordAuthenticationToken) user).getPrincipal();
        return Objects.equals(castedUser.getTeam().getId(), teamId);
    }
}
