package com.harlyn.service;

import com.harlyn.domain.Team;
import com.harlyn.domain.chat.TeamChatMessage;
import com.harlyn.repository.TeamChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Created by wannabe on 14.12.15.
 */
@Service
public class TeamChatService {

	@Autowired
	private TeamChatMessageRepository teamChatMessageRepository;

	public TeamChatService setTeamChatMessageRepository(TeamChatMessageRepository teamChatMessageRepository) {
		this.teamChatMessageRepository = teamChatMessageRepository;
		return this;
	}

	public List<TeamChatMessage> getLastMessagesByTeam(Team team, int limit) {
		List<TeamChatMessage> teamChatMessages = teamChatMessageRepository.findAllByTeamOrderByIdDesc(team, new PageRequest(0, limit));
		Collections.reverse(teamChatMessages);
		return teamChatMessages;
	}

	public TeamChatMessage create(TeamChatMessage teamChatMessage) {
		return teamChatMessageRepository.saveAndFlush(teamChatMessage);
	}
}
