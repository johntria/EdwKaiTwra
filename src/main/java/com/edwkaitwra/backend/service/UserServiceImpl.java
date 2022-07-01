package com.edwkaitwra.backend.service;

import com.edwkaitwra.backend.config.exception.security.UserIsNotActivatedException;
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

import javax.management.relation.RoleNotFoundException;
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
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException, UserIsNotActivatedException {
        User user = userRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        this.isActivated(user, email);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRole().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        //We need to return the UserDetails User and not custom user implementation
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.isActivated(), true, true, true, authorities);
    }

    @Override
    public User saveUser(User user) {
        log.info("Saving new user:" + user + "to DB");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }


    @Override
    public void addRoleToUser(String email, String roleName) throws RoleNotFoundException {
        log.info("Adding role " + roleName + " to user " + email);
        User user = userRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Role role = roleRepo.findByName(roleName).orElseThrow(() -> new RoleNotFoundException("User not found"));


        //It's a transactional service, in .add we automatically save in DB
        user.getRole().add(role);
    }

    @Override
    public User getUserByEmail(String email) {
        log.info("Fetching User " + email);
        User user = userRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user;
    }

    @Override
    public List<User> getUsers() {
        log.info("Fetching All Users ");
        return userRepo.findAll();
    }

    @Override
    public void isActivated(User user, String email) {
        if (!user.isActivated()) {
            log.error("User " + email + " is not activated");
            throw new UserIsNotActivatedException("Your account is not activated, " + "Please look at your emails ");
        }

    }

    @Override
    public void isActivatedByEmail(String email) {
        User user = userRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        this.isActivated(user, email);
    }

    public void userExists(Optional<User> user, String email) {
        if (user.isEmpty()) {
            log.error("User not found " + email);
            throw new UsernameNotFoundException("User not found");
        } else {
            log.info("User found in DB " + user.get().getEmail());
        }
    }


}
