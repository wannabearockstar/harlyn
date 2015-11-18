package com.harlyn.service;

import com.harlyn.domain.Team;
import com.harlyn.domain.User;
import com.harlyn.exception.NonUniqueTeamNameException;
import com.harlyn.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by wannabe on 18.11.15.
 */
@Service
public class TeamService {
    @Autowired
    private TeamRepository teamRepository;

    public Team createTeam(String name, User captain) {
        if (isTeamNameExists(name)) {
            throw new NonUniqueTeamNameException(name);
        }
        return teamRepository.saveAndFlush(new Team(name, captain));
    }

    public TeamService setTeamRepository(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
        return this;
    }

    protected boolean isTeamNameExists(String name) {
        return teamRepository.teamWithNameExist(name);
    }
}
