package com.harlyn.repository;

import com.harlyn.domain.chat.CompetitionChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by wannabe on 14.12.15.
 */
@Repository
public interface CompetititonChatMessageRepository extends JpaRepository<CompetitionChatMessage, Long> {
}
