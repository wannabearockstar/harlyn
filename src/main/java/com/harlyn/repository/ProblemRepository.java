package com.harlyn.repository;

import com.harlyn.domain.problems.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by wannabe on 20.11.15.
 */
@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {
}
