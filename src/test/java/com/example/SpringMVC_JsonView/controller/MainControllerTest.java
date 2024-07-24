package com.example.SpringMVC_JsonView.controller;

import com.example.SpringMVC_JsonView.model.Order;
import com.example.SpringMVC_JsonView.model.User;
import com.example.SpringMVC_JsonView.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.empty;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MainController.class)
public class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private User correctUser;
    private Order userOrder;

    @BeforeEach
    public void setup() {
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
    public void getAllUsers_OK() throws Exception {
        List<User> users = Collections.singletonList(correctUser);
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John"))
                .andExpect(jsonPath("$[0].email").value("john@example.com"))
                .andExpect(jsonPath("$[0].*", hasSize(3)));
    }

    @Test
    public void getAllUsers_EmptyList() throws Exception {
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(empty())));
    }

    @Test
    public void getUserInfo_OK() throws Exception {
        when(userService.getUser(1L)).thenReturn(correctUser);

        mockMvc.perform(get("/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.orders", hasSize(1)))
                .andExpect(jsonPath("$.orders[0].product").value("Laptop"))
                .andExpect(jsonPath("$.orders[0].amount").value(1000.0))
                .andExpect(jsonPath("$.*", hasSize(4)));

    }

    @Test
    public void сreateUser() throws Exception {
        when(userService.createUser(any(User.class))).thenReturn(correctUser);

        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(correctUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    public void testUpdateUser() throws Exception {
        when(userService.updateUser(Mockito.eq(1L), any(User.class))).thenReturn(correctUser);

        mockMvc.perform(put("/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(correctUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
