package com.example.SpringMVC_JsonView.model.repository;

import com.example.SpringMVC_JsonView.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
}
