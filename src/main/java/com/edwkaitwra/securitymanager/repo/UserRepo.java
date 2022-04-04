package com.edwkaitwra.securitymanager.repo;

import com.edwkaitwra.securitymanager.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
}
