package com.harlyn.repository;

import com.harlyn.domain.Team;
import com.harlyn.domain.TeamInvite;
import com.harlyn.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by wannabe on 18.11.15.
 */
@Repository
public interface TeamInviteRepository extends JpaRepository<TeamInvite, Long> {
    @Query(value = "select case when count(invite) > 0 then true else false end from TeamInvite invite where invite.team = :team and invite.recipent = :recipent")
    boolean inviteExists(@Param("team") Team team, @Param("recipent") User recipent);

    TeamInvite findOneByRecipentAndTeam(User recipent, Team team);
}
