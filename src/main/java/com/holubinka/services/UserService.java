package com.holubinka.services;

import com.holubinka.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    Optional<User> create(User user);

    Optional<List<User>> getAll();
}
