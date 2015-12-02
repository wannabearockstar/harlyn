package com.harlyn.repository;

import com.harlyn.domain.Team;
import com.harlyn.domain.competitions.Competition;
import com.harlyn.domain.competitions.RegisteredTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by wannabe on 02.12.15.
 */
public interface RegisteredTeamRepository extends JpaRepository<RegisteredTeam, Long> {
    RegisteredTeam findOneByCompetitionAndTeam(Competition competition, Team team);

    @Modifying(clearAutomatically = true)
    @Query(value = "update RegisteredTeam registered_team set registered_team.points = registered_team.points + :points where registered_team.id = :id")
    void incrementTeamPoints(@Param("id") Long registeredTeamId, @Param("points")Integer points);
}
