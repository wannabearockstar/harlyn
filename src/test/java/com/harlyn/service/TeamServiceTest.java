package com.harlyn.service;

import com.harlyn.HarlynApplication;
import com.harlyn.domain.Team;
import com.harlyn.domain.User;
import com.harlyn.exception.NonUniqueTeamNameException;
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

import static org.junit.Assert.*;

/**
 * Created by wannabe on 18.11.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HarlynApplication.class)
@TransactionConfiguration
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, FlywayTestExecutionListener.class})
@WebAppConfiguration
@ActiveProfiles({"test"})
public class TeamServiceTest {
    @Autowired
    private Flyway flyway;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private UserRepository userRepository;

    private TeamService teamService;

    @Before
    public void setUp() throws Exception {
        flyway.clean();
        flyway.migrate();
        teamService = new TeamService();
        teamService.setTeamRepository(teamRepository);
    }

    @After
    public void tearDown() throws Exception {
        flyway.clean();
    }

    @Test
    public void testCreateTeam() throws Exception {
        User captain = userRepository.saveAndFlush(new User("captain@cap.cap", "captain", "i am captain"));
        Team team = teamService.createTeam("team1", captain);
        assertEquals("team1", teamRepository.findOneByName("team1").getName());

        try {
            teamService.createTeam("team1", captain);
            fail("Duplicate team name");
        } catch (NonUniqueTeamNameException name) {
            assertTrue(true);
        }
    }
}