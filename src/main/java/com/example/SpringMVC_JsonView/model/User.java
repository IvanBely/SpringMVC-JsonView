package com.example.SpringMVC_JsonView.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(UserView.Public.class)
    private Long id;

    @NotBlank(message = "Name is mandatory", groups = ValidationGroups.Create.class)
    @JsonView(UserView.Public.class)
    private String name;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is mandatory", groups = ValidationGroups.Create.class)
    @JsonView(UserView.Public.class)
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonView(UserView.Details.class)
    private List<Order> orders;
}
