package com.harlyn.service;

import com.harlyn.HarlynApplication;
import com.harlyn.domain.User;
import com.harlyn.domain.chat.CompetitionChatMessage;
import com.harlyn.domain.competitions.Competition;
import com.harlyn.repository.CompetitionChatMessageRepository;
import com.harlyn.repository.CompetitionRepository;
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
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by wannabe on 14.12.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HarlynApplication.class)
@TransactionConfiguration
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, FlywayTestExecutionListener.class})
@WebAppConfiguration
@ActiveProfiles({"test"})
public class CompetitionChatServiceTest {

	@Autowired
	PlatformTransactionManager platformTransactionManager;
	TransactionTemplate transactionTemplate;
	@Autowired
	private Flyway flyway;
	@Autowired
	private CompetitionChatMessageRepository competitionChatMessageRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CompetitionRepository competitionRepository;
	private CompetitionChatService competitionChatService;

	@Before
	public void setUp() throws Exception {
		flyway.clean();
		flyway.migrate();
		competitionChatService = new CompetitionChatService()
			.setCompetitionChatMessageRepository(competitionChatMessageRepository);
		transactionTemplate = new TransactionTemplate(platformTransactionManager);
	}

	@After
	public void tearDown() throws Exception {
		flyway.clean();
	}

	@Test
	public void testGetLastMessagesByCompetition() throws Exception {
		//given
		User user = userRepository.saveAndFlush(new User("email@email.com", "usrnm", "password"));
		Competition competition = competitionRepository.saveAndFlush(new Competition("name"));
		Competition competition2 = competitionRepository.saveAndFlush(new Competition("name2"));
		competitionChatMessageRepository.saveAndFlush(new CompetitionChatMessage("c1", new Date(), user, competition));
		competitionChatMessageRepository.saveAndFlush(new CompetitionChatMessage("c2", new Date(), user, competition));
		competitionChatMessageRepository.saveAndFlush(new CompetitionChatMessage("c3", new Date(), user, competition));
		competitionChatMessageRepository.saveAndFlush(new CompetitionChatMessage("c4", new Date(), user, competition2));
		competitionChatMessageRepository.saveAndFlush(new CompetitionChatMessage("c5", new Date(), user, competition));
		//when
		List<CompetitionChatMessage> competitionChatMessages = competitionChatService.getLastMessagesByCompetition(competition, 3);
		//then
		assertEquals(3, competitionChatMessages.size());
		assertEquals("c2", competitionChatMessages.get(0).getContent());
		assertEquals("c3", competitionChatMessages.get(1).getContent());
		assertEquals("c5", competitionChatMessages.get(2).getContent());
	}

	@Test
	public void testPurgeMessage() throws Exception {
		//given
		User user = userRepository.saveAndFlush(new User("email@email.com", "usrnm", "password"));
		Competition competition = competitionRepository.saveAndFlush(new Competition("name"));
		CompetitionChatMessage ccm = competitionChatMessageRepository.saveAndFlush(new CompetitionChatMessage("content", new Date(), user, competition));
		//when
		ccm = competitionChatMessageRepository.findOne(ccm.getId());
		//then
		assertEquals("content", ccm.getContent());
		//when
		final CompetitionChatMessage finalCcm = ccm;
		transactionTemplate.execute(status -> {
			competitionChatService.purgeMessage(finalCcm.getId());
			return 1;
		});
		//then
		assertNull(competitionChatMessageRepository.findOne(ccm.getId()));

	}
}