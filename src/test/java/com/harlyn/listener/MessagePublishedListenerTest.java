package com.harlyn.listener;

import com.harlyn.HarlynApplication;
import com.harlyn.config.WebSocketConfig;
import com.harlyn.domain.Team;
import com.harlyn.domain.User;
import com.harlyn.domain.chat.ChatMessage;
import com.harlyn.domain.chat.CompetitionChatMessage;
import com.harlyn.domain.chat.TeamChatMessage;
import com.harlyn.domain.competitions.Competition;
import com.harlyn.event.MessagePublishedEvent;
import com.harlyn.repository.*;
import org.flywaydb.core.Flyway;
import org.flywaydb.test.junit.FlywayTestExecutionListener;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

import static org.mockito.Mockito.*;

/**
 * Created by wannabe on 15.12.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HarlynApplication.class)
@TransactionConfiguration
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, FlywayTestExecutionListener.class})
@WebAppConfiguration
@ActiveProfiles({"test"})
public class MessagePublishedListenerTest {
    @Autowired
    private Flyway flyway;
    @Autowired
    private CompetitionChatMessageRepository competitionChatMessageRepository;
    @Autowired
    private TeamChatMessageRepository teamChatMessageRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CompetitionRepository competitionRepository;
    @Resource
    private Map<Class<? extends ChatMessage>, WebSocketConfig.MessageEndpointResolver> chatEndpointResolvers;
    private SimpMessagingTemplate simpMessagingTemplate;
    private MessagePublishedListener messagePublishedListener;

    @Before
    public void setUp() throws Exception {
        flyway.clean();
        flyway.migrate();
        simpMessagingTemplate = mock(SimpMessagingTemplate.class);
        messagePublishedListener = new MessagePublishedListener()
                .setTemplate(simpMessagingTemplate)
                .setChatEndpointResolvers(chatEndpointResolvers);
    }

    @After
    public void tearDown() throws Exception {
        flyway.clean();
    }

    @Test
    public void testOnApplicationEvent() throws Exception {
        //given
        Competition competition = competitionRepository.saveAndFlush(new Competition("name1"));
        User user = userRepository.saveAndFlush(new User("email@emai.com", "usrnm", "psswd"));
        Team team = teamRepository.saveAndFlush(new Team("name", user));
        CompetitionChatMessage competitionChatMessage = competitionChatMessageRepository.saveAndFlush(new CompetitionChatMessage("content1", new Date(), user, competition));
        TeamChatMessage teamChatMessage = teamChatMessageRepository.saveAndFlush(new TeamChatMessage("content2", new Date(), user, team));
        MessagePublishedEvent competitionEvent = new MessagePublishedEvent(this, competitionChatMessage);
        MessagePublishedEvent teamEvent = new MessagePublishedEvent(this, teamChatMessage);
        //when
        messagePublishedListener.onApplicationEvent(competitionEvent);
        //then
        verify(simpMessagingTemplate, times(1)).convertAndSend(
                "/out/competition." + competition.getId(),
                competitionChatMessage
        );
        //when
        messagePublishedListener.onApplicationEvent(teamEvent);
        //then
        verify(simpMessagingTemplate, times(1)).convertAndSend(
                "/out/team." + team.getId(),
                teamChatMessage
        );
    }
}