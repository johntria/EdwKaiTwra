package com.edwkaitwra.backend.repo;

import com.edwkaitwra.backend.domain.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepo extends JpaRepository<Email, Long> {
}
