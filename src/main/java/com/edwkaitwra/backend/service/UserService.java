package com.edwkaitwra.backend.service;

import com.edwkaitwra.backend.domain.Role;
import com.edwkaitwra.backend.domain.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);

    Role saveRole(Role role);

    void addRoleToUser(String email, String roleName);

    User getUser(String email);

    List<User> getUsers();
}
