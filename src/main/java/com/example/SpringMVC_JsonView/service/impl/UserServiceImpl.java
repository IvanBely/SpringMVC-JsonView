package com.example.SpringMVC_JsonView.service.impl;

import com.example.SpringMVC_JsonView.exception.UserNotFoundException;
import com.example.SpringMVC_JsonView.model.User;
import com.example.SpringMVC_JsonView.model.repository.UserRepository;
import com.example.SpringMVC_JsonView.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    @Override
    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }
    @Override
    public User createUser(User user) {
        user.setOrders(Collections.emptyList());
        userRepository.save(user);
        return user;
    }
    @Override
    public User updateUser(Long id, User user) {

        User userNewDetails = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        boolean isUpdated = false;

        if (!user.getName().isEmpty() && !user.getName().equals(userNewDetails.getName())) {
            userNewDetails.setName(user.getName());
            isUpdated = true;
        }
        if (!user.getEmail().isEmpty() && !user.getEmail().equals(userNewDetails.getEmail())) {
            userNewDetails.setEmail(user.getEmail());
            isUpdated = true;
        }
        if (!isUpdated) {
            return userNewDetails;
        }
        return userRepository.save(userNewDetails);
    }

    @Override
    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new UserNotFoundException("User not found with id: " + id);
        }
    }
}
