package com.edwkaitwra.backend.service;

import com.edwkaitwra.backend.domain.Role;

public interface RoleService {
    void saveByName(String role);

    Role findByName(String name);

}
