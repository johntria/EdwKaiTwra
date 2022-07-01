package com.edwkaitwra.backend.service;

import com.edwkaitwra.backend.domain.User;

import javax.management.relation.RoleNotFoundException;
import java.util.List;

public interface UserService {
    User saveUser(User user);


    void addRoleToUser(String email, String roleName) throws RoleNotFoundException;

    User getUserByEmail(String email);

    List<User> getUsers();

    void isActivated(User user, String email);

    void isActivatedByEmail(String email);


}
