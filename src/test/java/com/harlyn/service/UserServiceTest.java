package com.harlyn.service;

import com.harlyn.HarlynApplication;
import com.harlyn.domain.User;
import com.harlyn.exception.NonUniqueUserDataException;
import com.harlyn.repository.RoleRepository;
import com.harlyn.repository.UserRepository;
import org.flywaydb.core.Flyway;
import org.flywaydb.test.annotation.FlywayTest;
import org.flywaydb.test.junit.FlywayTestExecutionListener;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.validation.ConstraintViolationException;
import java.util.Objects;

import static org.junit.Assert.*;

/**
 * Created by wannabe on 17.11.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HarlynApplication.class)
@TransactionConfiguration
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, FlywayTestExecutionListener.class})
@WebAppConfiguration
@ActiveProfiles({"test"})
public class UserServiceTest {
	@Autowired
	PlatformTransactionManager platformTransactionManager;
	TransactionTemplate transactionTemplate;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private Flyway flyway;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepository roleRepository;

	private UserService userService;

	@Before
	public void setUp() throws Exception {
		flyway.clean();
		flyway.migrate();
		userService = new UserService();
		userService.setUserRepository(userRepository)
			.setPasswordEncoder(passwordEncoder)
			.setRoleRepository(roleRepository)
		;
		transactionTemplate = new TransactionTemplate(platformTransactionManager);
	}

	@Test
	@FlywayTest
	public void testFindUserByEmail() throws Exception {
		userRepository.saveAndFlush(new User("email@email.com", "username", "password"));

		assertEquals("username", userService.findUserByEmail("email@email.com").getUsername());
		assertNull(userService.findUserByEmail("no@existing.com"));
	}

	@Test
	@FlywayTest
	public void testCreateUser() throws Exception {
		userService.createUser(new User("test@email.com", "username", "password"));
		assertEquals("username", userRepository.findOneByEmail("test@email.com").getUsername());
		assertTrue(passwordEncoder.matches("password", userRepository.findOneByEmail("test@email.com").getPassword()));
	}

	@Test
	@FlywayTest
	public void testValidateUniqueData() throws Exception {
		userService.createUser(new User("test@email.com", "username", "password"));
		try {
			userService.validateUniqueData(new User("test@email.com", "username1", "password"));
		} catch (NonUniqueUserDataException e) {
			if (!Objects.equals(e.getMessage(), "Email already taken")) {
				fail("Duplicate email");
			}
			assertTrue(true);
		}

		try {
			userService.validateUniqueData(new User("tes1t@email.com", "username", "password"));
		} catch (NonUniqueUserDataException e) {
			if (!Objects.equals(e.getMessage(), "Username already taken")) {
				fail("Duplicate username");
			}
			assertTrue(true);
		}
	}

	@After
	public void tearDown() throws Exception {
		flyway.clean();
	}

	@Test
	public void testCreateUserWithInvalidData() throws Exception {
		try {
			userService.createUser(new User("", "username", "password"));
			fail("Create user with invalid data");
		} catch (ConstraintViolationException e) {
			assertTrue(true);
		}

		try {
			userService.createUser(new User("s", "", "password"));
			fail("Create user with invalid data");
		} catch (ConstraintViolationException e) {
			assertTrue(true);
		}

		try {
			userService.createUser(new User("d", "username", ""));
			fail("Create user with invalid data");
		} catch (ConstraintViolationException e) {
			assertTrue(true);
		}

		try {
			userService.createUser(new User("d", "username", null));
			fail("Create user with invalid data");
		} catch (ConstraintViolationException e) {
			assertTrue(true);
		}
	}

	@Test
	public void testCreateAdmin() throws Exception {
		userService.createUser(new User("test@email.com", "username", "password"));
		assertFalse(userRepository.findOneByEmail("test@email.com").hasRole("ROLE_ADMIN"));
	}

	@Test
	public void testBanUser() throws Exception {
		//given
		User user = userService.createUser(new User("email@email.com", "username", "password"));
		//when
		user = userRepository.findOne(user.getId());
		//then
		assertFalse(user.isBannedInChat());
		//when
		final User finalUser = user;
		transactionTemplate.execute(s -> {
			userService.banUser(finalUser);
			return 1;
		});
		user = userRepository.findOne(user.getId());
		//then
		assertTrue(user.isBannedInChat());
	}
}