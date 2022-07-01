package com.edwkaitwra.backend.api.secured;

import com.edwkaitwra.backend.domain.Role;
import com.edwkaitwra.backend.domain.User;
import com.edwkaitwra.backend.dto.RoleToUserForm;
import com.edwkaitwra.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;


    @GetMapping("/users")
//    @RolesAllowed({GODUSER, USER})
    public ResponseEntity<List<User>> getUser() {
        return ResponseEntity.ok().body(userService.getUsers());
    }




}

