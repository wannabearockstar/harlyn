package com.harlyn.service;

import com.harlyn.domain.Team;
import com.harlyn.domain.User;
import com.harlyn.domain.competitions.Competition;
import com.harlyn.domain.competitions.RegisteredTeam;
import com.harlyn.exception.TeamAlreadyRegisteredException;
import com.harlyn.exception.UserNotCaptainException;
import com.harlyn.repository.CompetitionRepository;
import com.harlyn.repository.RegisteredTeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by wannabe on 02.12.15.
 */
@Service
public class CompetitionService {
    @Autowired
    private CompetitionRepository competitionRepository;
    @Autowired
    private RegisteredTeamRepository registeredTeamRepository;

    public CompetitionService setCompetitionRepository(CompetitionRepository competitionRepository) {
        this.competitionRepository = competitionRepository;
        return this;
    }

    public CompetitionService setRegisteredTeamRepository(RegisteredTeamRepository registeredTeamRepository) {
        this.registeredTeamRepository = registeredTeamRepository;
        return this;
    }

    public Competition findById(long id) {
        return competitionRepository.findOne(id);
    }

    public void registerUserTeamToCompetition(Competition competition, User user) {
        if (!user.getTeam().getCaptain().equals(user)) {
            throw new UserNotCaptainException();
        }
        RegisteredTeam registeredTeam = registeredTeamRepository.findOneByCompetitionAndTeam(competition, user.getTeam());
        if (registeredTeam != null) {
            throw new TeamAlreadyRegisteredException();
        }
        registeredTeamRepository.saveAndFlush(new RegisteredTeam(competition, user.getTeam()));
    }

    public List<Competition> findAll() {
        return competitionRepository.findAll();
    }

    public List<Competition> findAllAvailableCompetitions(Date currentDate) {
        return competitionRepository.findAllByCurrentDate(currentDate);
    }

    public boolean isCompetitionAvailable(final Competition competition, Date currentDate) {
        if (competition.getEndDate() == null && competition.getStartDate() == null) {
            return true;
        }
        if (competition.getEndDate() == null) {
            return competition.getStartDate().before(currentDate);
        }
        if (competition.getStartDate() == null) {
            return competition.getEndDate().after(currentDate);
        }
        return competition.getStartDate().before(currentDate) && competition.getEndDate().after(currentDate);
    }

    public boolean isTeamRegistered(Competition competition, Team team) {
        return registeredTeamRepository.existsByCompetitionAndTeam(competition, team);
    }
}
