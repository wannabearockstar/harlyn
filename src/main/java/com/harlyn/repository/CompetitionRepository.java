package com.harlyn.repository;

import com.harlyn.domain.competitions.Competition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by wannabe on 02.12.15.
 */
@Repository
public interface CompetitionRepository extends JpaRepository<Competition, Long> {

	@Query(value = "select competition " +
		"from Competition competition " +
		"where (competition.startDate is null and competition.endDate is null) " +
		"or (competition.startDate is null and competition.endDate > :current_date)" +
		"or (competition.startDate < :current_date and competition.endDate is null) " +
		"or (competition.startDate < :current_date and competition.endDate > :current_date)" +
		"order by competition.id desc"
	)
	List<Competition> findAllByCurrentDate(@Param("current_date") Date currentDate);
}
