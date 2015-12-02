package com.harlyn.service;

import com.harlyn.HarlynApplication;
import com.harlyn.domain.Team;
import com.harlyn.domain.User;
import com.harlyn.domain.competitions.Competition;
import com.harlyn.domain.competitions.RegisteredTeam;
import com.harlyn.exception.TeamAlreadyRegisteredException;
import com.harlyn.exception.UserNotCaptainException;
import com.harlyn.repository.CompetitionRepository;
import com.harlyn.repository.RegisteredTeamRepository;
import com.harlyn.repository.TeamRepository;
import com.harlyn.repository.UserRepository;
import org.flywaydb.core.Flyway;
import org.flywaydb.test.junit.FlywayTestExecutionListener;
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
 * Created by wannabe on 02.12.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HarlynApplication.class)
@TransactionConfiguration
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, FlywayTestExecutionListener.class})
@WebAppConfiguration
@ActiveProfiles({"test"})
public class CompetitionServiceTest {
    @Autowired
    private Flyway flyway;
    @Autowired
    private CompetitionRepository competitionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private RegisteredTeamRepository registeredTeamRepository;
    private CompetitionService competitionService;

    @Before
    public void setUp() throws Exception {
        flyway.clean();
        flyway.migrate();
        competitionService = new CompetitionService()
                .setCompetitionRepository(competitionRepository)
                .setRegisteredTeamRepository(registeredTeamRepository);
    }

    @Test
    public void testFindById() throws Exception {
        Competition competition = competitionRepository.saveAndFlush(new Competition("name"));

        assertEquals("name", competitionService.findById(competition.getId()).getName());
    }

    @Test
    public void testRegisterUserTeamToCompetition() throws Exception {
        Competition competition = competitionRepository.saveAndFlush(new Competition("name"));
        User captain = userRepository.saveAndFlush(new User("captain@email.com", "captain", "captain"));
        Team team = teamRepository.saveAndFlush(new Team("name", captain));
        User user = userRepository.saveAndFlush(new User("user@email.com", "user", "user").setTeam(team));
        captain = userRepository.saveAndFlush(captain.setTeam(team));
        try {
            competitionService.registerUserTeamToCompetition(competition, user);
            fail("User register team as no captain");
        } catch (UserNotCaptainException e) {
            assertTrue(true);
        }
        competitionService.registerUserTeamToCompetition(competition, captain);
        competition = competitionRepository.findOne(competition.getId());
        RegisteredTeam registeredTeam = competition.getRegisteredTeams()
                .iterator()
                .next();

        assertTrue(registeredTeam.getTeam().equals(team));
        assertTrue(registeredTeam.getCompetition().equals(competition));

        try {
            competitionService.registerUserTeamToCompetition(competition, captain);
            fail("Same team registered twice for competition");
        } catch (TeamAlreadyRegisteredException e) {
            assertTrue(true);
        }
    }
}