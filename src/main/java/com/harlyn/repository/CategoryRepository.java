package com.harlyn.repository;

import com.harlyn.domain.problems.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wannabe on 08.12.15.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

	List<Category> findAllByOrderByIdDesc();

	@Query(value = "SELECT *\n" +
		"FROM categories\n" +
		"  RIGHT JOIN (\n" +
		"               SELECT category_id\n" +
		"               FROM problems\n" +
		"               WHERE competition_id = :competition_id\n" +
		"             ) comp_categories ON id = comp_categories.category_id\n" +
		"WHERE id IS NOT NULL", nativeQuery = true)
	List<Category> findAllByCompetition(@Param("competition_id") Long competitionId);
}
