package com.example.SpringMVC_JsonView.service;

import com.example.SpringMVC_JsonView.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    User getUser(Long id);

    User createUser(User user);

    User updateUser(Long id, User user);

    void deleteUser(Long id);

}
