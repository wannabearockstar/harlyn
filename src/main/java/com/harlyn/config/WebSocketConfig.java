package com.harlyn.config;

import com.harlyn.domain.chat.ChatMessage;
import com.harlyn.domain.chat.CompetitionChatMessage;
import com.harlyn.domain.chat.TeamChatMessage;
import com.harlyn.security.ValidTeamHandshakeInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wannabe on 14.12.15.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/out");
        config.setApplicationDestinationPrefixes("/in");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.setInterceptors(
                teamHandshakeInterceptor()
        );
    }

    @Bean
    public ChannelInterceptor teamHandshakeInterceptor() {
        return new ValidTeamHandshakeInterceptor();
    }

    /**
     * Get endpoint of socket broker to corresponding {@link ChatMessage} instance
     * @return Endpoint, generated from {@link ChatMessage} data
     */
    @Bean
    public Map<Class<? extends ChatMessage>, MessageEndpointResolver> chatEndpointResolvers() {
        Map<Class<? extends ChatMessage>, MessageEndpointResolver> resolvers = new HashMap<>();
        resolvers.put(CompetitionChatMessage.class,
                message -> "/out/competition." + ((CompetitionChatMessage) message).getCompetition().getId()
        );
        resolvers.put(TeamChatMessage.class,
                message -> "/out/team." + ((TeamChatMessage) message).getTeam().getId()
        );
        return resolvers;
    }

    @FunctionalInterface
    public interface MessageEndpointResolver {
        String resolveEndpoint(ChatMessage message);
    }
}
