package com.harlyn.repository;

import com.harlyn.domain.problems.Hint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by wannabe on 12.03.16.
 */
@Repository
public interface HintRepository extends JpaRepository<Hint, Long> {

}
