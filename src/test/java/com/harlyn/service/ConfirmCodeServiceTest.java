package com.harlyn.service;

import com.harlyn.HarlynApplication;
import com.harlyn.domain.ConfirmCode;
import com.harlyn.domain.User;
import com.harlyn.repository.ConfirmCodeRepository;
import com.harlyn.repository.UserRepository;
import org.apache.velocity.app.VelocityEngine;
import org.flywaydb.core.Flyway;
import org.flywaydb.test.junit.FlywayTestExecutionListener;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by wannabe on 17.11.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HarlynApplication.class)
@TransactionConfiguration
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, FlywayTestExecutionListener.class})
@WebAppConfiguration
@ActiveProfiles({"test"})
public class ConfirmCodeServiceTest {

	@Autowired
	PlatformTransactionManager platformTransactionManager;
	TransactionTemplate transactionTemplate;
	@Autowired
	private Flyway flyway;
	@Autowired
	private ConfirmCodeRepository confirmCodeRepository;
	@Mock
	private JavaMailSender mailSender;
	@Autowired
	private SimpleMailMessage templateConfirmCodeMessage;
	@Autowired
	private VelocityEngine velocityEngine;
	@Autowired
	private UserRepository userRepository;

	private ConfirmCodeService confirmCodeService;

	@Before
	public void setUp() throws Exception {
		flyway.clean();
		flyway.migrate();
		confirmCodeService = new ConfirmCodeService();
		mailSender = mock(JavaMailSenderImpl.class);
		confirmCodeService.setConfirmCodeRepository(confirmCodeRepository)
			.setMailSender(mailSender)
			.setTemplateConfirmCodeMessage(templateConfirmCodeMessage)
			.setVelocityEngine(velocityEngine)
			.setUserRepository(userRepository);
		transactionTemplate = new TransactionTemplate(platformTransactionManager);
	}

	@After
	public void tearDown() throws Exception {
		flyway.clean();
	}

	@Test
	public void testCreateConfirmCode() throws Exception {
		User user = userRepository.saveAndFlush(new User("email@emai.com", "username", "password"));
		ConfirmCode confirmCode = confirmCodeService.createConfirmCode(user);

		assertEquals("username", confirmCodeRepository.findOneByCode(confirmCode.getCode()).getUser().getUsername());
		verify(mailSender, times(1)).send(any(MimeMessagePreparator.class));
	}

	@Test
	public void testConfirmUserByCode() throws Exception {
		User user = userRepository.saveAndFlush(new User("email@emai.com", "username", "password"));
		ConfirmCode confirmCode = confirmCodeService.createConfirmCode(user);

		assertFalse(userRepository.findOneByEmail("email@emai.com").isEnabled());
		assertNotNull(confirmCodeRepository.findOneByCode(confirmCode.getCode()));

		transactionTemplate.execute(status -> {
			confirmCodeService.confirmUserByCode(confirmCode.getCode());
			return 1;
		});

		assertTrue(userRepository.findOneByEmail("email@emai.com").isEnabled());
		assertNull(confirmCodeRepository.findOneByCode(confirmCode.getCode()));
	}
}