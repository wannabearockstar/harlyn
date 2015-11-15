package com.harlyn.repository;

import com.harlyn.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by wannabe on 15.11.15.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select user from User user where user.email = :email")
    User findUserByEmail(@Param("email") String email);
}
