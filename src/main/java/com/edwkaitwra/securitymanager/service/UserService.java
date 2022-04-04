package com.edwkaitwra.securitymanager.service;

import com.edwkaitwra.securitymanager.domain.Role;
import com.edwkaitwra.securitymanager.domain.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    Role saveRole(Role role);
    void addRoleToUser(String email,String roleName);
    User getUser(String email);
    List<User> getUsers();
}
