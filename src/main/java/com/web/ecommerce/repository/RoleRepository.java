package com.web.ecommerce.repository;

import com.web.ecommerce.model.Role;
import com.web.ecommerce.model.enums.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName(AppRole appRole);
}
