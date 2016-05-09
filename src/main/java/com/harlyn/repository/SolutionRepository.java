package com.harlyn.repository;

import com.harlyn.domain.User;
import com.harlyn.domain.problems.Solution;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wannabe on 20.11.15.
 */
@Repository
public interface SolutionRepository extends JpaRepository<Solution, Long> {

	List<Solution> findByCheckedFalseOrderByIdDesc();

	List<Solution> findAllByOrderByIdDesc();
	@Query("SELECT CASE WHEN COUNT(u) > 0 THEN 'true' ELSE 'false' END FROM Solution u WHERE u.solver = :solver")
	boolean existsBySolver(@Param("solver") User solver);

	@EntityGraph(value = "fullSolution", type = EntityGraph.EntityGraphType.LOAD)
	Solution getById(Long id);
}
