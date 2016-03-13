package com.harlyn.repository;

import com.harlyn.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wannabe on 18.11.15.
 */
@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

	Team findOneByName(String name);

	@Query(value = "select case when count(team) > 0 then true else false end from Team team where team.name = :name")
	boolean teamWithNameExist(@Param("name") String name);

	List<Team> findAllByOrderByIdDesc();
}
