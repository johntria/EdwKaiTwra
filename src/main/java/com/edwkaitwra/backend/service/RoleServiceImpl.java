package com.edwkaitwra.backend.service;

import com.edwkaitwra.backend.config.exception.RoleAlreadyExistsException;
import com.edwkaitwra.backend.config.exception.RoleNotFoundException;
import com.edwkaitwra.backend.domain.Role;
import com.edwkaitwra.backend.repo.RoleRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepo roleRepo;

    @Override
    public void saveByName(String role) {
        log.info("Saving new role: " + role + "to DB");
        if (roleRepo.findByName(role).isPresent()) {
            log.info("Role " + role + " already exists");
            throw new RoleAlreadyExistsException("Role " + role + " already exists");
        } else {
            roleRepo.saveByName(role);
        }
    }

    @Override
    public Role findByName(String name) {
        return roleRepo.findByName(name).orElseThrow(() -> new RoleNotFoundException("Role " + name + " not found"));
    }


}
