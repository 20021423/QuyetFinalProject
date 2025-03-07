package com.example.ntquyet.service;

import com.example.ntquyet.model.User;
import java.util.Optional;

public interface IUserService {
    Optional<User> findByUsername(String username);
    User registerUser(User user);
}
