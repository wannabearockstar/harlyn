package com.harlyn.repository;

import com.harlyn.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by wannabe on 28.11.15.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

	Role findOneByName(String roleName);
}
