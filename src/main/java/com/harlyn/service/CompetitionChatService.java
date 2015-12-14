package com.harlyn.service;

import com.harlyn.domain.chat.CompetitionChatMessage;
import com.harlyn.domain.competitions.Competition;
import com.harlyn.repository.CompetitionChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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
        return competitionChatMessageRepository.findAllByCompetitionOrderByIdDesc(competition, new PageRequest(0, limit));
    }
}
