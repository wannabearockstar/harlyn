package com.harlyn.repository;

import com.harlyn.domain.chat.CompetitionChatMessage;
import com.harlyn.domain.competitions.Competition;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wannabe on 14.12.15.
 */
@Repository
public interface CompetitionChatMessageRepository extends JpaRepository<CompetitionChatMessage, Long> {

	List<CompetitionChatMessage> findAllByCompetitionOrderByIdDesc(Competition competition, Pageable pageable);

	@Modifying
	@Query(value = "update CompetitionChatMessage ccm set ccm.content ='' where ccm.id = :id")
	void setBlankContent(@Param("id") Long id);
}
