package com.harlyn.service;

import com.harlyn.HarlynApplication;
import com.harlyn.domain.Team;
import com.harlyn.domain.User;
import com.harlyn.domain.chat.TeamChatMessage;
import com.harlyn.repository.TeamChatMessageRepository;
import com.harlyn.repository.TeamRepository;
import com.harlyn.repository.UserRepository;
import org.flywaydb.core.Flyway;
import org.flywaydb.test.junit.FlywayTestExecutionListener;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by wannabe on 14.12.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HarlynApplication.class)
@TransactionConfiguration
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, FlywayTestExecutionListener.class})
@WebAppConfiguration
@ActiveProfiles({"test"})
public class TeamChatServiceTest {

	@Autowired
	private Flyway flyway;
	@Autowired
	private TeamChatMessageRepository teamChatMessageRepository;
	@Autowired
	private TeamRepository teamRepository;
	@Autowired
	private UserRepository userRepository;
	private TeamChatService teamChatService;

	@Before
	public void setUp() throws Exception {
		flyway.clean();
		flyway.migrate();
		teamChatService = new TeamChatService().setTeamChatMessageRepository(teamChatMessageRepository);
	}

	@After
	public void tearDown() throws Exception {
		flyway.clean();
	}

	@Test
	public void testGetLastMessagesByTeam() throws Exception {
		//given
		User user = userRepository.saveAndFlush(new User("email@email.com", "usrnm", "password"));
		Team team = teamRepository.saveAndFlush(new Team("name1", user));
		Team team2 = teamRepository.saveAndFlush(new Team("name2"));
		teamChatMessageRepository.saveAndFlush(new TeamChatMessage("c1", new Date(), user, team));
		teamChatMessageRepository.saveAndFlush(new TeamChatMessage("c2", new Date(), user, team));
		teamChatMessageRepository.saveAndFlush(new TeamChatMessage("c3", new Date(), user, team));
		teamChatMessageRepository.saveAndFlush(new TeamChatMessage("c4", new Date(), user, team2));
		teamChatMessageRepository.saveAndFlush(new TeamChatMessage("c5", new Date(), user, team));
		//when
		List<TeamChatMessage> teamChatMessages = teamChatService.getLastMessagesByTeam(team, 3);
		//then
		assertEquals(3, teamChatMessages.size());
		assertEquals("c2", teamChatMessages.get(0).getContent());
		assertEquals("c3", teamChatMessages.get(1).getContent());
		assertEquals("c5", teamChatMessages.get(2).getContent());
	}
}