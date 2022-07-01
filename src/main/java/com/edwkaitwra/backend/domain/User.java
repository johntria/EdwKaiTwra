package com.edwkaitwra.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //We also use it as a username !!
    @Column(unique = true)
    @Email
    private String email;

    @JsonIgnore
    @NotNull
    @Size(min = 60, max = 60)
    @Column(name = "password_hash")
    private String password;

    private String firstName;

    private LocalDateTime createdDay;

    private String lastName;

    @NotNull
    private boolean activated = false;

    @Size(max = 256)
    @Column(name = "image_url")
    private String imageUrl;

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> role = new ArrayList<>();

    public User(String email, String password, String firstName, LocalDateTime createdDay, String lastName, boolean activated, String imageUrl, Collection<Role> role) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.createdDay = createdDay;
        this.lastName = lastName;
        this.activated = activated;
        this.imageUrl = imageUrl;
        this.role = role;
    }
}
