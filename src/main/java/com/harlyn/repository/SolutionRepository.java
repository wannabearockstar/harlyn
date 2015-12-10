package com.harlyn.repository;

import com.harlyn.domain.problems.Solution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wannabe on 20.11.15.
 */
@Repository
public interface SolutionRepository extends JpaRepository<Solution, Long> {
    List<Solution> findByCheckedFalseOrderByIdDesc();
    List<Solution> findAllByOrderByIdDesc();
}
