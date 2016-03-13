package com.harlyn.repository;

import com.harlyn.domain.problems.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wannabe on 08.12.15.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

	List<Category> findAllByOrderByIdDesc();
}
