package com.harlyn.domain.chat;

import com.harlyn.domain.User;
import com.harlyn.domain.competitions.Competition;

import java.util.Date;

/**
 * Created by wannabe on 22.03.16.
 */
public class CompetitionLeaderboardChatMessage extends ChatMessage {
	private final Competition competition;

	public CompetitionLeaderboardChatMessage(String content, Date postedAt, User author, Competition competition) {
		super(content, postedAt, author);
		this.competition = competition;
	}

	public Competition getCompetition() {
		return competition;
	}
}
