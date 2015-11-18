package com.harlyn.service;

import com.harlyn.domain.Team;
import com.harlyn.domain.TeamInvite;
import com.harlyn.domain.User;
import com.harlyn.exception.NonUniqueTeamNameException;
import com.harlyn.exception.UserAlreadyInTeamException;
import com.harlyn.exception.UserAlreadyInvitedException;
import com.harlyn.repository.TeamInviteRepository;
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

    @Autowired
    private TeamInviteRepository teamInviteRepository;

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

    public TeamService setTeamInviteRepository(TeamInviteRepository teamInviteRepository) {
        this.teamInviteRepository = teamInviteRepository;
        return this;
    }

    protected boolean isTeamNameExists(String name) {
        return teamRepository.teamWithNameExist(name);
    }

    public TeamInvite sendInvite(Team team, User user) {
        if (teamInviteRepository.inviteExists(team, user)) {
            throw new UserAlreadyInvitedException();
        }
        if (team.getUsers().contains(user)) {
            throw new UserAlreadyInTeamException();
        }
        return teamInviteRepository.saveAndFlush(new TeamInvite(team, user));
    }
}
