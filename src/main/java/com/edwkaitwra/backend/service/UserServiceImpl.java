package com.edwkaitwra.backend.service;

import com.edwkaitwra.backend.domain.Role;
import com.edwkaitwra.backend.domain.User;
import com.edwkaitwra.backend.repo.RoleRepo;
import com.edwkaitwra.backend.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepo.findByEmail(email);
        if (user.isEmpty()) {
            log.error("User not found " + email);
            throw new UsernameNotFoundException("User not found");
        } else {
            log.info("User found in DB " + user.get().getEmail());
        }
        User loadedUser = user.get();
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        loadedUser.getRole().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });

        //We need to return the UserDetails User and not custom user implementation
        return new org.springframework.security.core.userdetails.User(loadedUser.getEmail(), loadedUser.getPassword(), authorities);
    }

    @Override
    public User saveUser(User user) {
        log.info("Saving new user:" + user + "to DB");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving new role" + role + " to DB");
        return roleRepo.save(role);
    }

    @Override
    public void addRoleToUser(String email, String roleName) {
        log.info("Adding role " + roleName + " to user " + email);
        Optional<User> user = userRepo.findByEmail(email);
        if (user.isEmpty()) {
            log.error("User not found ");
            throw new UsernameNotFoundException("User not found");
        } else {
            log.info("User found in DB " + user.get().getEmail());
        }
        Role role = roleRepo.findByName(roleName);

        //It's a transactional service, in .add we automatically save in DB
        user.get().getRole().add(role);
    }

    @Override
    public User getUser(String email) {
        log.info("Fetching User " + email);
        Optional<User> user = userRepo.findByEmail(email);
        if (user.isEmpty()) {
            log.error("User not found ");
            throw new UsernameNotFoundException("User not found");
        } else {
            log.info("User found in DB " + user.get().getEmail());
        }
        return user.get();
    }

    @Override
    public List<User> getUsers() {
        log.info("Fetching All Users ");
        return userRepo.findAll();
    }


}
