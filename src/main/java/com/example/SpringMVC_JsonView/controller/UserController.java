package com.example.SpringMVC_JsonView.controller;

import com.example.SpringMVC_JsonView.model.User;
import com.example.SpringMVC_JsonView.model.UserView;
import com.example.SpringMVC_JsonView.model.ValidationGroups;
import com.example.SpringMVC_JsonView.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping("/")
    @JsonView(UserView.Public.class)
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> listUsers = userService.getAllUsers();
        return new ResponseEntity<>(listUsers, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    @JsonView(UserView.Details.class)
    public ResponseEntity<User> getUserInfo(@PathVariable Long id) {
        User user = userService.getUser(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    @PostMapping("/")
    public ResponseEntity<User> createUser(@Validated(ValidationGroups.Create.class) @RequestBody User user) {
        User createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Validated(ValidationGroups.Update.class) @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
