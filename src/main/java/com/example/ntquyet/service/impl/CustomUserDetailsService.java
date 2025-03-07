package com.example.ntquyet.security;

import com.example.ntquyet.model.User;
import com.example.ntquyet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // ✅ Kiểm tra role, nếu thiếu `ROLE_`, thêm vào
        String role = user.getRole();
        if (!role.startsWith("ROLE_")) {
            role = "ROLE_" + role; // Đảm bảo role luôn đúng format
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword()) // Mật khẩu đã mã hóa
                .roles(role.replace("ROLE_", "")) // ✅ Chỉ truyền role mà không có "ROLE_"
                .build();
    }
}
