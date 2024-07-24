package com.example.SpringMVC_JsonView.service;

import com.example.SpringMVC_JsonView.exception.UserNotFoundException;
import com.example.SpringMVC_JsonView.model.Order;
import com.example.SpringMVC_JsonView.model.User;
import com.example.SpringMVC_JsonView.model.repository.UserRepository;
import com.example.SpringMVC_JsonView.service.UserService;
import com.example.SpringMVC_JsonView.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User correctUser;
    private Order userOrder;

    @BeforeEach
    void setUp() {
        correctUser = new User();
        correctUser.setId(1L);
        correctUser.setName("John");
        correctUser.setEmail("john@example.com");

        userOrder = new Order();
        userOrder.setId(1L);
        userOrder.setUser(correctUser);
        userOrder.setProduct("Laptop");
        userOrder.setAmount(1000.0);

        correctUser.setOrders(Collections.singletonList(userOrder));
    }

    @Test
    void getAllUsers_OK() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(correctUser));

        List<User> users = userService.getAllUsers();
        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals(correctUser, users.get(0));
    }

    @Test
    void getUser_OK() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(correctUser));

        User foundUser = userService.getUser(1L);
        assertNotNull(foundUser);
        assertEquals(correctUser, foundUser);
    }

    @Test
    void getUser_UserNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUser(1L));
    }

    @Test
    void createUser_OK() {
        when(userRepository.save(any(User.class))).thenReturn(correctUser);

        User createdUser = userService.createUser(correctUser);
        assertNotNull(createdUser);
        assertEquals(correctUser, createdUser);
        assertEquals(0, createdUser.getOrders().size());
    }

    @Test
    void updateUser_OK() {
        User updatedUserDetails = new User();
        updatedUserDetails.setName("Jane Doe");
        updatedUserDetails.setEmail("jane.doe@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(correctUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUserDetails);

        User updatedUser = userService.updateUser(1L, updatedUserDetails);
        assertNotNull(updatedUser);
        assertEquals("Jane Doe", updatedUser.getName());
        assertEquals("jane.doe@example.com", updatedUser.getEmail());
    }

    @Test
    void updateUser_UserNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        User updatedUserDetails = new User();
        updatedUserDetails.setName("Jane Doe");
        updatedUserDetails.setEmail("jane.doe@example.com");

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(1L, updatedUserDetails));
    }

    @Test
    void deleteUser_OK() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        assertDoesNotThrow(() -> userService.deleteUser(1L));
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteUser_UserNotFoundException() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(1L));
    }
}
