package com.prozacto.vault.repository;

import com.prozacto.vault.model.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<ApplicationUser, Long>{
	ApplicationUser findByUsername(String username);
}
