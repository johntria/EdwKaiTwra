package com.edwkaitwra.backend.domain.dto;

import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class LoginDTO {
    @Nullable
    private String email;
    private String password;
}
