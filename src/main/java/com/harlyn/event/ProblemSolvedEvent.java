package com.harlyn.event;

import com.harlyn.domain.chat.CompetitionLeaderboardChatMessage;
import org.springframework.context.ApplicationEvent;

/**
 * Created by wannabe on 22.03.16.
 */
public class ProblemSolvedEvent extends ApplicationEvent {
	private final CompetitionLeaderboardChatMessage message;

	public ProblemSolvedEvent(Object source, CompetitionLeaderboardChatMessage message) {
		super(source);
		this.message = message;
	}

	public CompetitionLeaderboardChatMessage getCompetitionLeaderboardChatMessage() {
		return message;
	}
}
