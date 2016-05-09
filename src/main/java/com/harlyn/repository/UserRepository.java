package com.harlyn.repository;

import com.harlyn.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
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

	@Modifying
	@Query(value = "update User user set user.enabled = true where user.id = :user_id")
	void enableUserById(@Param("user_id") Long id);

	@Query(value = "select case when count(user) > 0 then true else false end from User user where user.email = :email")
	boolean userExistByEmail(@Param("email") String email);

	@Query(value = "select case when count(user) > 0 then true else false end from User user where user.username = :username")
	boolean userExistByUsername(@Param("username") String username);

	@EntityGraph(value = "fullUser", type = EntityGraph.EntityGraphType.LOAD)
	User findOneByEmail(String email);

	@Modifying
	@Query(value = "update User user set user.resetToken = :reset_token where user.id = :user_id")
	void updateResetToken(@Param("user_id") Long userId, @Param("reset_token") String resetToken);

	@EntityGraph(value = "fullUser", type = EntityGraph.EntityGraphType.LOAD)
	User findOneByResetToken(String resetToken);

	@Modifying
	@Query(value = "update User user set user.password = :password where user.id = :user_id")
	void updatePassword(@Param("user_id") Long id, @Param("password") String password);

	@Modifying
	@Query(value = "update User user set user.team = null where user.id = :user_id")
	void dropTeam(@Param("user_id") Long id);


	@Modifying
	@Query(value = "update User user set user.bannedInChat = true where user.id = :user_id")
	void setBannedTrue(@Param("user_id") Long id);
}
