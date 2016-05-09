package com.harlyn.service;

import com.harlyn.domain.User;
import com.harlyn.domain.chat.ChatMessage;
import com.harlyn.domain.chat.CompetitionChatMessage;
import com.harlyn.domain.competitions.Competition;
import com.harlyn.repository.CompetitionChatMessageRepository;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * Created by wannabe on 14.12.15.
 */
@Service
public class CompetitionChatService {

	@Autowired
	private CompetitionChatMessageRepository competitionChatMessageRepository;

	public CompetitionChatService setCompetitionChatMessageRepository(CompetitionChatMessageRepository competitionChatMessageRepository) {
		this.competitionChatMessageRepository = competitionChatMessageRepository;
		return this;
	}

	public List<CompetitionChatMessage> getLastMessagesByCompetition(Competition competition, int limit) {
		List<CompetitionChatMessage> competitionChatMessages = competitionChatMessageRepository.findAllByCompetitionOrderByIdDesc(competition, new PageRequest(0, limit));
		Collections.reverse(competitionChatMessages);
		return competitionChatMessages;
	}

	public CompetitionChatMessage create(CompetitionChatMessage competitionChatMessage) {
		competitionChatMessage.setContent(Jsoup.parse(competitionChatMessage.getContent()).text());
		return competitionChatMessageRepository.saveAndFlush(competitionChatMessage);
	}

	public CompetitionChatMessage getById(Long id) {
		return competitionChatMessageRepository.getOne(id);
	}

	@Transactional
	public void purgeMessage(Long id) {
		competitionChatMessageRepository.delete(id);
	}

	public ChatMessage getLastMessageByAuthor(User author) {
		return competitionChatMessageRepository.findByAuthorOrderByPostedAtDesc(author).stream()
			.findFirst().orElse(null);
	}
}
