package com.example.ntquyet.service.impl;

import com.example.ntquyet.model.User;
import com.example.ntquyet.repository.UserRepository;
import com.example.ntquyet.service.IUserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public User registerUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already taken!");
        }

        // ✅ Mã hóa mật khẩu trước khi lưu vào database
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // ✅ Gán ROLE_USER mặc định nếu chưa có
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("ROLE_USER");
        }

        return userRepository.save(user);
    }
}
