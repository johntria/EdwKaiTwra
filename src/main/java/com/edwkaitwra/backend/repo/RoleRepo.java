package com.edwkaitwra.backend.repo;

import com.edwkaitwra.backend.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);

    @Modifying
    @Query(value = "INSERT  INTO role (name) VALUES (:name) ", nativeQuery = true)
    void saveByName(@Param("name") String name);
}
