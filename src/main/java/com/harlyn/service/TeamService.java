package com.harlyn.service;

import com.harlyn.domain.Team;
import com.harlyn.domain.TeamInvite;
import com.harlyn.domain.User;
import com.harlyn.exception.*;
import com.harlyn.repository.SolutionRepository;
import com.harlyn.repository.TeamInviteRepository;
import com.harlyn.repository.TeamRepository;
import com.harlyn.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by wannabe on 18.11.15.
 */
@Service
public class TeamService {

	@Autowired
	private TeamRepository teamRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TeamInviteRepository teamInviteRepository;
	@Autowired
	private SolutionRepository solutionRepository;

	public Team createTeam(String name, User captain) {
		if (isTeamNameExists(name)) {
			throw new NonUniqueTeamNameException(name);
		}
		Team team = teamRepository.saveAndFlush(new Team(name, captain));
		userRepository.saveAndFlush(captain.setTeam(team));
		return team;
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

	@Transactional
	public void confirmInvite(TeamInvite teamInvite) {
		Team team = teamInvite.getTeam();
		User recipent = teamInvite.getRecipent();
		recipent.setTeam(team);
		recipent.getInvites().remove(teamInvite);
		teamInviteRepository.delete(teamInvite);
		teamInviteRepository.flush();
		userRepository.saveAndFlush(recipent);
	}

	public TeamInvite sendInvite(User captain, User recipent) {
		if (captain.getTeam() == null) {
			throw new UserWithoutTeamException();
		}
		if (captain.getTeam().getCaptain() != captain) {
			throw new UserInvalidTeamRightsException();
		}
		return sendInvite(captain.getTeam(), recipent);
	}

	public Team getById(Long teamId) {
		return teamRepository.findOne(teamId);
	}

	public TeamService setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
		return this;
	}

	public void confirmInvite(User recipent, Team team) {
		TeamInvite invite = teamInviteRepository.findOneByRecipentAndTeam(recipent, team);
		if (invite == null) {
			throw new MissingInviteException();
		}
		confirmInvite(invite);
	}

	public List<Team> getAllTeams() {
		return teamRepository.findAllByOrderByIdDesc();
	}

	public boolean userSolvedForTeam(User user) {
		return solutionRepository.existsBySolver(user);
	}

	@Transactional
	public void dropTeam(User user) {
		userRepository.dropTeam(user.getId());
	}
}
