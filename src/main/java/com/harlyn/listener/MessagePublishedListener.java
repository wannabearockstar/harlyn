package com.harlyn.listener;

import com.harlyn.config.WebSocketConfig;
import com.harlyn.domain.chat.ChatMessage;
import com.harlyn.event.MessagePublishedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by wannabe on 14.12.15.
 */
@Component
public class MessagePublishedListener implements ApplicationListener<MessagePublishedEvent> {
    @Autowired
    private SimpMessagingTemplate template;
    @Resource
    private Map<Class<? extends ChatMessage>, WebSocketConfig.MessageEndpointResolver> chatEndpointResolvers;

    public MessagePublishedListener setTemplate(SimpMessagingTemplate template) {
        this.template = template;
        return this;
    }

    public MessagePublishedListener setChatEndpointResolvers(Map<Class<? extends ChatMessage>, WebSocketConfig.MessageEndpointResolver> chatEndpointResolvers) {
        this.chatEndpointResolvers = chatEndpointResolvers;
        return this;
    }

    @Override
    public void onApplicationEvent(MessagePublishedEvent event) {
        template.convertAndSend(chatEndpointResolvers.get(event.getChatMessage().getClass())
                        .resolveEndpoint(event.getChatMessage()),
                event.getChatMessage()
        );
    }
}
