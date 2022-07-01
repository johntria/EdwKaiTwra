package com.edwkaitwra.backend.data;

import com.edwkaitwra.backend.config.exception.RoleAlreadyExistsException;
import com.edwkaitwra.backend.domain.User;
import com.edwkaitwra.backend.service.RoleService;
import com.edwkaitwra.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Value("${default-users.password}")
    private String defaultPassword;

    @Override
    public void run(String... args) throws Exception {
        loadDefaultRoles();
//        loadDefaultUsers();
    }

    private void loadDefaultRoles() {
        log.info("Stared initialization of default roles");

        List.of("ROLE_GODUSER", "ROLE_SUPERUSER", "ROLE_USER").forEach(role -> {
            try {
                roleService.saveByName(role);
            } catch (RoleAlreadyExistsException e) {
                log.info("Role " + role + " already exists");
            }
        });

        log.info("Ended initialization of default roles");
    }

    private void loadDefaultUsers() {

        log.info("Stared initialization of default users");
        List.of("goduser@edwkaitwra.gr", "superuser@edwkaitwra.gr", "user@edwkaitwra.gr").forEach(email -> {

            try {
                userService.getUserByEmail(email);
            } catch (UsernameNotFoundException e) {
                log.info("User " + email + " not found, started creation ");

                userService.saveUser(
                        new User(
                                email,
                                passwordEncoder.encode(this.defaultPassword),
                                "default",
                                LocalDateTime.now(),
                                "default",
                                true,
                                "",
                                List.of(roleService.findByName("ROLE_" + email.substring(0, email.indexOf('@')).toUpperCase()))
                        )
                );
            }


        });


        log.info("Ended initialization of default users");
    }
}
