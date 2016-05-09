package com.harlyn.repository;

import com.harlyn.domain.problems.Problem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by wannabe on 20.11.15.
 */
@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {

	@Query(value = "select problem " +
		"from Problem problem " +
		"where (problem.startDate is null and problem.endDate is null) " +
		"or (problem.startDate is null and problem.endDate > :current_date)" +
		"or (problem.startDate < :current_date and problem.endDate is null) " +
		"or (problem.startDate < :current_date and problem.endDate > :current_date)" +
		"order by problem.id desc"
	)
	List<Problem> findAllByCurrentDate(@Param("current_date") Date currentDate);

	@EntityGraph(value = "problemWithoutUsers", type = EntityGraph.EntityGraphType.LOAD)
	Problem getById(Long id);

	@EntityGraph(value = "fullProblem", type = EntityGraph.EntityGraphType.LOAD)
	Problem findOneById(Long id);
}
