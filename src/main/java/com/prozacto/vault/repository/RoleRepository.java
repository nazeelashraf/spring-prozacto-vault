package com.prozacto.vault.repository;

import com.prozacto.vault.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long>{
	Role findByName(String name);
}
