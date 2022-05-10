package com.edwkaitwra.backend.dto;

import com.edwkaitwra.backend.domain.Role;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
public class UserDTO {

    private Long id;

    private String email;

    private String firstName;

    private LocalDateTime createdDay;

    private String lastName;

    private boolean activated;

    private String imageUrl;

    private Collection<Role> role;
}
