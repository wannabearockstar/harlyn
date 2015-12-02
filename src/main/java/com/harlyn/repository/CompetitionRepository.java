package com.harlyn.repository;

import com.harlyn.domain.competitions.Competition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by wannabe on 02.12.15.
 */
@Repository
public interface CompetitionRepository extends JpaRepository<Competition, Long> {
}
