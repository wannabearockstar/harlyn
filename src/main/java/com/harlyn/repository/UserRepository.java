package com.harlyn.repository;

import com.harlyn.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Modifying
    @Query(value = "update User user set user.enabled = true where user.id = :user_id")
    void enableUserById(@Param("user_id") Integer id);

    @Query(value = "select case when count(user) > 0 then true else false end from User user where user.email = :email")
    boolean userExistByEmail(@Param("email") String email);

    @Query(value = "select case when count(user) > 0 then true else false end from User user where user.username = :username")
    boolean userExistByUsername(@Param("username") String username);
}
