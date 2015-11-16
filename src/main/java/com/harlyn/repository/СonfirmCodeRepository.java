package com.harlyn.repository;

import com.harlyn.domain.ConfirmCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by wannabe on 16.11.15.
 */
@Repository
public interface СonfirmCodeRepository extends JpaRepository<ConfirmCode, Long> {
}
