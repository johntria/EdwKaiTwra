package com.edwkaitwra.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    public static final String GODUSER = "ROLE_GODUSER";
    public static final String SUPERUSER = "ROLE_SUPERUSER";
    public static final String USER = "ROLE_USER";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
}
