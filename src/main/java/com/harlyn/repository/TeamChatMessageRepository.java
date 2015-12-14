package com.harlyn.repository;

import com.harlyn.domain.Team;
import com.harlyn.domain.chat.TeamChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wannabe on 14.12.15.
 */
@Repository
public interface TeamChatMessageRepository extends JpaRepository<TeamChatMessage, Long> {
    List<TeamChatMessage> findAllByTeamOrderByIdDesc(Team team, Pageable pageable);
}
